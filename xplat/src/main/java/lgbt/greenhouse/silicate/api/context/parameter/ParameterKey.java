package lgbt.greenhouse.silicate.api.context.parameter;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.type.ValueType;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A key referring to a parameter in {@link ParameterMap}.
 * @param <T> type of {@link ValueType}
 */
public sealed interface ParameterKey<T>
		permits GlobalParameterKey,
		LocalParameterKey,
		ParameterKey.Direct,
		ParameterKey.Reference {
	Codec<ParameterKey<?>> TEMPLATE_CODEC = ParameterTemplate.CODEC
			.xmap(
					template -> new Reference<>(template.id()),
					key -> new ParameterTemplate(key.getId())
			);
	Codec<ParameterKey.Reference<?>> CODEC = ResourceLocation.CODEC
			.xmap(
					id -> {
						if (id.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
							return new Reference<>(SilicateConstants.id(id.getPath()));
						} else {
							return new Reference<>(id);
						}
					},
					Reference::getId
			);

	static <V> ParameterKey.Direct<V> direct(V value) {
		return new Direct<>(value);
	}

	ResourceLocation getId();

	/**
	 * @return the {@link ValueType} for this {@link ParameterKey}
	 * @apiNote This is {@code null} <i>only</i> if it extends {@link Reference}. This ensures that {@link Reference} can be looked up dynamically. Prefer {@link #getType(GameContext)} unless you know what you're doing.
	 */
	@Nullable ValueType<T> getTypeStatic();

	/**
	 * @param gameContext the context in which this {@link ParameterKey} exists used for resolving references
	 * @return the {@link ValueType} for this {@link ParameterKey}
	 */
	default ValueType<T> getType(GameContext gameContext) {
		return Objects.requireNonNull(this.getTypeStatic(), Reference.NULL_STATIC_PARAMETER_KEY_TYPES);
	}

	/**
	 * A deferred reference to a {@link ParameterKey}.
	 * @param <T> type of {@link ValueType}
	 */
	final class Reference<T> implements ParameterKey<T> {
		private static final String NULL_STATIC_PARAMETER_KEY_TYPES = "Static types in non-reference Parameter Keys shall never be null";
		private final ResourceLocation id;

		public Reference(ResourceLocation id) {
			this.id = id;
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public @Nullable ValueType<T> getTypeStatic() {
			return null;
		}

		@Override
		public ValueType<T> getType(GameContext gameContext) {
			return Objects.requireNonNull(Objects.requireNonNull(gameContext.getParameterMap().resolve(this)).getTypeStatic(), NULL_STATIC_PARAMETER_KEY_TYPES);
		}
	}

	final class Direct<T> implements ParameterKey<T> {
		private final T value;

		private Direct(T value) {
			this.value = value;
		}

		@Override
		public ResourceLocation getId() {
			return SilicateConstants.id("direct");
		}

		@Override
		public @Nullable ValueType<T> getTypeStatic() {
			return null;
		}

		@Override
		public ValueType<T> getType(GameContext gameContext) {
			//noinspection unchecked // why the hell is this not already Class<T>
			return new ValueType<>((Class<T>) this.getValue().getClass(), null);
		}

		public T getValue() {
			return value;
		}
	}
}
