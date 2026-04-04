package lgbt.greenhouse.silicate.api.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.predicate.meta.BaseCodec;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.predicate.minecraft.EntityHasPassengerPredicate;
import lgbt.greenhouse.silicate.api.predicate.minecraft.EntityHasVehiclePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandle;
import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
 * <h2>{@link Type}</h2>
 * This is a class required for registering a {@link GamePredicate} as it holds
 * important information such as codecs.
 * @see EntityHasPassengerPredicate
 * @see EntityHasVehiclePredicate
 */
public interface GamePredicate<T extends GamePredicate<T>> extends Predicate<GameContext> {
	/**
	 * Do <b>not</b> use this outside Silicate. Your code <b>will</b> break, and you will <b>not</b> get support, <b>at all</b>!
	 * <br>
	 * This is strictly an internal {@link Codec}.
	 * @see #CODEC
	 */
	@ApiStatus.Internal
	Codec<GamePredicate<?>> DISPATCH_CODEC = SilicateBuiltInRegistries.PREDICATE.byNameCodec()
			.dispatch("predicate", GamePredicate::getType, Type::createCodec);
	/**
	 * A {@link Codec} representing conditions (a {@link Holder} to a {@link GamePredicate} or JSON-defined {@link SilicateRegistries#CONDITION}).
	 * <br>
	 * Conditions are usages of {@link GamePredicate}s in the form of a dispatch.
	 */
	Codec<Holder<GamePredicate<?>>> CODEC = RegistryFileCodec.create(
			SilicateRegistries.CONDITION,
			PredicateCodecs.OR_BOOLEAN_DISPATCH_CODEC
	);

	@Override
	boolean test(GameContext ctx);

	/// Pushes to the [GameContext], tests this [GamePredicate], then pops.
	/// # ⚠️ Warning: ⚠️
	/// **Do not use this** unless you are iterating or immediately returning! Using this
	/// otherwise can cause scope leak. As always, ensure any modification to
	/// [ParameterMap] is inside push-pop calls.
	/// @param ctx the current [GameContext]
	/// @return the result of the [#test(GameContext)]
	default boolean pushTestPop(GameContext ctx) {
		ctx.pushParameterMap();
		boolean result = this.test(ctx);
		ctx.popParameterMap();
		return result;
	}

	/**
	 * @return the codec responsible for condition configuration.
	 */
	default MapCodec<T> getCodec() {
		return getType().getCodec();
	}

	Type<T> getType();

	/**
	 * A utility class used in codec definition for creating {@link GamePredicate}s (subclasses).
	 * @param <T> The type of the {@link GamePredicate} subclass.
	 * @see SilicateBuiltInRegistries#PREDICATE
	 */
	abstract class Type<T extends GamePredicate<T>> {
		private final MapCodec<T> codec = this.createCodec();

		/// Creates a [BaseCodec] for inheritors of a predicate to append
		/// default fields with.
		/// # Usage
		///
		/// Return a super-call to this method chained with a call to [BaseCodec#andThen(BaseCodec)], passing your desired [BaseCodec].
		/// ## Example
		///
		/// {@snippet lang=java :
		///  @Override
		///  BaseCodec<t> createBaseCodec() {
		///      return super.createBaseCodec()
		///           .andThen(builder, builder.withField(
		///  	                "conditions",
		///  	                SilicateValueTypes.LIST_CONDITION,
		///  	                Codec.list(GamePredicate.CODEC),
		///  	                CompoundPredicate::getConditions
		///           ));
		///  }
		///  }</t>
		protected BaseCodec<T> createBaseCodec() {
			return builder -> builder;
		}

		/// The [MapCodec] of the [GamePredicate], built with [#createBaseCodec()].
		/// ## Usage
		///
		/// Call [#createBaseCodec()], chaining calls to [BaseCodec#apply(PredicateCodecBuilder)] and [PredicateCodecBuilder#build(MethodHandle)].
		/// ## Example
		///
		/// {@snippet lang = java:
		///  import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;
		///  @Override
		///  public MapCodec<allpredicate> createCodec() {
		///  	return this.createBaseCodec()
		///  		.apply(PredicateCodecBuilder.of(lgbt.greenhouse.silicate.api.predicate.std.AlwaysPredicate.class))
		///  		.withValue(
		///  			"value",
		///  			SilicatePrimitives.BOOLEAN,
		///  			AlwaysPredicate::value
		///  		)
		///  		.build(PredicateCodecBuilder.findConstructor(AlwaysPredicate.class, boolean.class));
		///  }
		/// }
		/// <br>
		/// If you only have a public constructor, you can just use [PredicateCodecBuilder#build()] without any parameters.
		/// {@snippet lang = java:
		///  import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
		///  @Override
		///  public MapCodec<notpredicate> createCodec() {
		///  	return createBaseCodec()
		///  		.apply(lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder.of(NotPredicate.class))
		///  		.withValue(
		///  			"condition",
		///  			SilicateValueTypes.CONDITION,
		///  			GamePredicate.CODEC,
		///  			NotPredicate::condition
		///  		)
		///  		.build();
		///  }
		/// }</notpredicate></allpredicate>
		protected abstract MapCodec<T> createCodec();

		/**
		 * The {@link MapCodec} of the {@link GamePredicate}, built with {@link #createCodec()}.
		 */
		public final MapCodec<T> getCodec() {
			return this.codec;
		}
	}
}
