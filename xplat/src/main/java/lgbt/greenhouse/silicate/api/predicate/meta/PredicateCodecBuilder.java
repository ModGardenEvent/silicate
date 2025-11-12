package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import dev.lukebemish.codecextras.record.KeyedRecordCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.*;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import lgbt.greenhouse.silicate.api.type.ValueType;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import lgbt.greenhouse.silicate.impl.cursed.Clazzy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A builder for {@link MapCodec} for {@link GamePredicate} inheritors.
 * @param <T> the type of the predicate to build a codec for
 */
public final class PredicateCodecBuilder<T extends GamePredicate<T>> {
	private static final Logger LOGGER = SilicateConstants.getLogger(PredicateCodecBuilder.class);
	private static final ValueType<Deferred<?>> DEFERRED =
		new ValueType<>(
				Clazzy.cast(Deferred.class),
				Deferred.CODEC
		);

	private final Class<T> clazz;
	private final Map<String, FieldEntry<?, ?, ?>> fields = new HashMap<>();
	private final List<FieldEntry<?, ?, ?>> entries = new ArrayList<>();

	private PredicateCodecBuilder(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Construct a new builder for a {@link GamePredicate} codec.
	 * @param clazz the class of the {@link GamePredicate} this builder is building for
	 * @return a new {@link PredicateCodecBuilder}
	 * @param <T> the {@link GamePredicate} this builder is building for
	 */
	public static <T extends GamePredicate<T>> PredicateCodecBuilder<T> of(Class<T> clazz) {
		return new PredicateCodecBuilder<>(clazz);
	}

	/**
	 * Find a public constructor {@link MethodHandle} for a class.
	 * @param clazz the owning class
	 * @param parameters the constructor's parameters
	 * @return a {@link MethodHandle} referring to the class's constructor
	 */
	public static MethodHandle findConstructor(Class<?> clazz, Class<?>... parameters) {
		return findConstructor(MethodHandles.publicLookup(), clazz, parameters);
	}

	/**
	 * Find a private constructor {@link MethodHandle} for a class.
	 * @param lookup a private {@link MethodHandles.Lookup} in the owning class
	 * @param clazz the owning class
	 * @param parameters the constructor's parameters
	 * @return a {@link MethodHandle} referring to the class's constructor
	 * @implSpec The private {@link MethodHandles.Lookup} passed to this method <b>must only</b> be used for finding the desired method. Anything more is malicious and should be reported to your nearest Encapsulation Enforcement Officer.
	 */
	public static MethodHandle findPrivateConstructor(MethodHandles.Lookup lookup, Class<?> clazz, Class<?>... parameters) {
		return findConstructor(lookup, clazz, parameters);
	}

	private static MethodHandle findConstructor(MethodHandles.Lookup lookup, Class<?> clazz, Class<?>... parameters) {
		try {
			return lookup.findConstructor(clazz, MethodType.methodType(void.class, parameters));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Find a public static method {@link MethodHandle} for a class.
	 * @param clazz the owning class
	 * @param name the name of the method
	 * @param returnType the method's return type
	 * @param parameters the method's parameters
	 * @return a {@link MethodHandle} referring to the static method
	 */
	public static MethodHandle findStaticMethod(Class<?> clazz, String name, Class<?> returnType, Class<?>... parameters) {
		return findStaticMethod(MethodHandles.publicLookup(), clazz, name, returnType, parameters);
	}

	/**
	 * Find a private static method {@link MethodHandle} for a class.
	 * @param lookup a private {@link MethodHandles.Lookup} in the owning class
	 * @param clazz the owning class
	 * @param name the name of the method
	 * @param returnType the method's return type
	 * @param parameters the method's parameters
	 * @return a {@link MethodHandle} referring to the static method
	 * @implSpec The private {@link MethodHandles.Lookup} passed to this method <b>must only</b> be used for finding the desired method. Anything more is malicious and should be reported to your nearest Encapsulation Enforcement Officer.
	 */
	public static MethodHandle findPrivateStaticMethod(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> returnType, Class<?>... parameters) {
		return findStaticMethod(lookup, clazz, name, returnType, parameters);
	}

	private static MethodHandle findStaticMethod(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> returnType, Class<?>... parameters) {
		try {
			return lookup.findStatic(clazz, name, MethodType.methodType(returnType, parameters));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Add a parameter to this predicate.
	 * <br>
	 * A parameter is a type of value in a predicate that cannot be represented constantly.
	 * Instead, it is represented by parameter keys.
	 * @param key field key
	 * @param type value type
	 * @param <V> underlying type of field
	 * @see ParameterTemplate
	 */
	public <V> PredicateCodecBuilder<T> withParameter(String key, ValueType<V> type, Function<T, ParameterKey<V>> getter) {
		this.fields.put(key, new FieldEntry<>(
				key,
				type,
				null,
				getter,
				false,
				Optional.empty(),
				true,
				null,
				false
		));
		this.addOrderedEntry(key);
		return this;
	}

	/**
	 * Add a reference to this predicate.
	 * <br>
	 * A reference is a type of value in a predicate that represents a
	 * {@link ParameterKey.Reference} constantly.
	 * @param key field key
	 * @see ParameterTemplate
	 * @see #withParameter(String, ValueType, Function)
	 * @see #withDynamicValue(String, String, Function)
	 */
	public PredicateCodecBuilder<T> withReference(String key, Function<T, ParameterKey.Reference<?>> getter) {
		this.fields.put(key, new FieldEntry<>(
				key,
				SilicateValueTypes.REFERENCE_KEY,
				SilicateValueTypes.REFERENCE_KEY.codec(),
				getter,
				false,
				Optional.empty(),
				false,
				null,
				true
		));
		this.addOrderedEntry(key);
		return this;
	}

	/**
	 * Add a dynamically typed value field to this predicate.
	 * @param key field key
	 * @param getter getter in {@link GamePredicate}
	 */
	public PredicateCodecBuilder<T> withDynamicValue(
			String valueTypeKey,
			String key,
			Function<T, DynamicValue> getter
	) {
		this.fields.put(key, new FieldEntry<>(
				key,
				SilicateValueTypes.ANY,
				null,
				getter,
				false,
				Optional.empty(),
				false,
				new DynamicEntry(valueTypeKey),
				true
		));
		this.addOrderedEntry(key);
		return this;
	}

	/**
	 * Add a value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param getter getter in {@link GamePredicate}
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withValue(String key, ValueType<V> type, Function<T, Deferred<V>> getter) {
		return withValue(key, type, Objects.requireNonNull(type.codec(), "ValueType must have a codec"), getter);
	}

	/**
	 * Add a value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param codec value codec
	 * @param getter getter in {@link GamePredicate}
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withValue(String key, ValueType<V> type, Codec<V> codec,  Function<T, Deferred<V>> getter) {
		this.fields.put(key, new FieldEntry<>(
				key,
				type,
				codec,
				getter,
				false,
				Optional.empty(),
				false,
				null,
				false
		));
		this.addOrderedEntry(key);
		return this;
	}

	/**
	 * Add an optional value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param getter getter in {@link GamePredicate}
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withOptionalValue(String key, ValueType<V> type,  Function<T, Deferred<Optional<V>>> getter) {
		return withOptionalValue(key, type, Objects.requireNonNull(type.codec(), "ValueType must have a codec"), getter);
	}

	/**
	 * Add an optional value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param getter getter in {@link GamePredicate}
	 * @param defaultValue default value
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withDefaultOptionalValue(String key, ValueType<V> type, Function<T, Deferred<V>> getter, V defaultValue) {
		return withDefaultOptionalValue(key, type, Objects.requireNonNull(type.codec(), "ValueType must have a codec"), getter, defaultValue);
	}

	/**
	 * Add an optional value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param codec value codec
	 * @param getter getter in {@link GamePredicate}
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withOptionalValue(String key, ValueType<V> type, Codec<V> codec,  Function<T, Deferred<Optional<V>>> getter) {
		this.fields.put(key, new FieldEntry<>(
				key,
				type,
				codec,
				getter,
				true,
				Optional.empty(),
				false,
				null,
				false
		));
		this.addOrderedEntry(key);
		return this;
	}

	/**
	 * Add an optional value field to this predicate.
	 * @param key field key
	 * @param type value type
	 * @param codec value codec
	 * @param getter getter in {@link GamePredicate}
	 * @param defaultValue default value
	 * @param <V> underlying type of value
	 */
	public <V> PredicateCodecBuilder<T> withDefaultOptionalValue(String key, ValueType<V> type, Codec<V> codec, Function<T, Deferred<V>> getter, V defaultValue) {
		this.fields.put(key, new FieldEntry<>(
				key,
				type,
				codec,
				getter,
				true,
				Optional.of(defaultValue),
				false,
				null,
				false
		));
		this.addOrderedEntry(key);
		return this;
	}

	private void addOrderedEntry(String key) {
		this.entries.add(this.fields.get(key));
	}

	/**
	 * Build this codec into a {@link MapCodec}.
	 * @return a {@link MapCodec} for the {@link GamePredicate}
	 */
	public MapCodec<T> build() {
		Class<?>[] parameters = this.entries.stream()
				.map(fieldEntry -> {
					if (fieldEntry.requiresTemplateValues) {
						return new ValueType<>(ParameterKey.class, null);
					} else if (fieldEntry.passThroughType) {
						return fieldEntry.type;
					} else {
						return DEFERRED;
					}
				})
				.map(ValueType::clazz)
				.toArray(i -> new Class<?>[i]);
		return this.build(findConstructor(this.clazz, parameters));
	}

	/**
	 * Build this codec into a {@link MapCodec}.
	 * @param constructor a {@link MethodHandle} to a constructor with every value field in order, returning the {@link GamePredicate}
	 * @return a {@link MapCodec} for the {@link GamePredicate}
	 */
	@SuppressWarnings("UnstableApiUsage") // it's okay
	public MapCodec<T> build(MethodHandle constructor) {
		// if you're trying to read this code, god help you
		// let the cursed begin
		return KeyedRecordCodecBuilder.mapCodec(
				builder -> {
					List<KeyedRecordCodecBuilder.Key<?>> keys = new ArrayList<>();
					for (var entry : this.entries) {
						// These types need to be erased anyway. They don't matter in an array.
						//noinspection unchecked
						FieldEntry<T, Object, Object> field = (FieldEntry<T, @NotNull Object, @NotNull Object>) entry;
						MapCodec<Object> fieldCodec;
						if (field.codec == null) {
							if (field.requiresTemplateValues) {
								// It has to be unchecked since ParameterKey<?> has a wildcard type
								// and that is incompatible with Object
								//noinspection unchecked
								keys.add(builder.add(
										(MapCodec<Object>) (Object) ParameterKey.TEMPLATE_CODEC.fieldOf(field.key),
										field.getter
								));
							} else if (field.dynamic != null) {
								// this is fucking cursed
								// edit: still cursed as fuck
								// edit 2: check the createCodec source code for a surprise :)
								keys.add(builder.add(
										DynamicValue.createCodec(field.dynamic.key, field.key),
										field.getter.andThen(a -> (DynamicValue) a)
								));
							} else {
								LOGGER.warn(
										"{}: Value is somehow neither dynamic nor templated and yet has no codec. What did you do?",
										this.clazz.getName()
								);
							}

							continue;
						}

						if (!field.optional) {
							fieldCodec = field.codec.fieldOf(field.key);
						} else {
							if (field.defaultValue.isEmpty()) {
								// Similarly, we do not care about Optional.
								//noinspection unchecked
								fieldCodec = (MapCodec<Object>) (Object) field.codec.optionalFieldOf(field.key);
							} else {
								fieldCodec = field.codec.optionalFieldOf(field.key, field.defaultValue.get());
							}
						}

						if (!field.type.equals(SilicateValueTypes.REFERENCE_KEY)) {
							// so you can use ParameterKeys in place of values
							//noinspection unchecked // cursed
							fieldCodec = (MapCodec<Object>) (Object) Codec.mapEither(
									fieldCodec,
									Deferred.CODEC.fieldOf(field.key)
							);
						}

						keys.add(builder.add(fieldCodec, field.getter));
					}

					// add definitions field
					keys.add(builder.add(
							Definitions.CODEC,
							t -> null // we don't need a getter for this
					));

					return container -> {
						Object[] fields1 = keys.stream()
								.filter(key -> {
									// remove `definitions` thing from predicate constructor call
									Object object = container.get(key);
									return !(object instanceof Definitions);
								})
								.map(key -> {
									// ensure we can allow ParameterKeys in place of values,
									// then compensate for such cases
									Object obj = container.get(key);
									if (obj instanceof ParameterKey<?> parameterKey) {
										return parameterKey;
									} else if (obj instanceof DynamicValue dynamicValue) {
										return dynamicValue;
									}

									@SuppressWarnings("unchecked") // cursed
									var either = (Either<Object, Deferred<?>>) obj;

									if (either.right().isPresent()) {
										return either.right().get();
									} else {
										return new Deferred<>(either.orThrow());
									}
								})
								.toArray();
						Optional<Definitions> definitions = keys.stream()
								.filter(key -> {
									Object object = container.get(key);
									return object instanceof Definitions;
								})
								.map(key -> (Definitions) container.get(key))
								.findAny();
						try {
							// T's type is checked at runtime
							//noinspection unchecked
							return (T) new DynamicMetaPredicate<>(
									(T) constructor.invokeWithArguments(fields1),
									ctx -> {
										if (definitions.isEmpty()) return;

										ctx.getParameterMap().addAll(ParameterMap.of(
												definitions.get().map().entrySet().stream().map(entry ->
																new AbstractMap.SimpleEntry<>(
																		new LocalParameterKey<>(
																				entry.getKey(),
																				entry.getValue().type()
																		),
																		new Parameter<>(entry.getValue().value().get(ctx))
																))
														.collect(Collectors.toMap(
																Map.Entry::getKey,
																Map.Entry::getValue
														))
										));
									}
							);
						} catch (Throwable e) {
							throw new RuntimeException(e);
						}
					};
				}
		);
	}

	private record FieldEntry<O, T, V>(
			String key,
			ValueType<T> type,
			@Nullable Codec<T> codec,
			Function<O, V> getter,
			boolean optional,
			Optional<T> defaultValue,
			boolean requiresTemplateValues,
			@Nullable DynamicEntry dynamic,
			boolean passThroughType
	) {}

	private record DynamicEntry(
			String key
	) {}
}
