package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.meta.BaseCodec;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityPassengerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityVehiclePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
 * <h2>{@link TypedGamePredicate}</h2>
 * If a predicate has a parameter type, it is recommended to implement {@link TypedGamePredicate} so that conditions that chain it can automatically determine their parameter type.
 * <h2>{@link Type}</h2>
 * This is a class required for registering a {@link GamePredicate} as it holds
 * important information such as codecs.
 * @see EntityPassengerPredicate
 * @see EntityVehiclePredicate
 * @see TypedGamePredicate
 * @see MaybeTypedPredicate
 */
public interface GamePredicate<T extends GamePredicate<T>> extends Predicate<GameContext> {
	Codec<GamePredicate<?>> TYPED_CODEC = SilicateBuiltInRegistries.PREDICATE.byNameCodec()
			.dispatch("predicate", GamePredicate::getType, GamePredicate.Type::getCodec);
	Codec<Holder<GamePredicate<?>>> CODEC = RegistryFileCodec.create(SilicateRegistries.CONDITION_TEMPLATE, TYPED_CODEC);

	@Override
	boolean test(GameContext context);

	/**
	 * @return the codec responsible for condition configuration.
	 */
	@NotNull default MapCodec<T> getCodec() {
		return getType().getCodec();
	}

	@NotNull Type<T> getType();

	/**
	 * A utility class used in codec definition for creating {@link GamePredicate}s (subclasses).
	 * @param <T> The type of the {@link GamePredicate} subclass.
	 * @see SilicateBuiltInRegistries#PREDICATE
	 */
	abstract class Type<T extends GamePredicate<T>> {
		private final MapCodec<T> codec = this.getCodec();

		/**
		 * Creates a {@link BaseCodec} for inheritors of a predicate to append
		 * default fields with.
		 *
		 * <h1>Usage</h1>
		 * Return a super-call to this method chained with a call to {@link BaseCodec#andThen(BaseCodec)}, passing your desired {@link BaseCodec}.
		 * <h2>Example</h2>
		 * {@snippet lang=java :
		 * @Override
		 * BaseCodec<T> createBaseCodec() {
		 *     return super.createBaseCodec()
		 *          .andThen(builder -> builder.withField(
		 * 	                "conditions",
		 * 	                SilicateValueTypes.LIST_CONDITION,
		 * 	                Codec.list(GamePredicate.CODEC),
		 * 	                CompoundPredicate::getConditions
		 *          ));
		 * }
		 * }
		 */
		protected BaseCodec<T> createBaseCodec() {
			return builder -> builder;
		}

		/**
		 * The {@link MapCodec} of the {@link GamePredicate}, built with {@link #createBaseCodec()}.
		 * <h2>Usage</h2>
		 * Call {@link #createBaseCodec()}, chaining calls to {@link BaseCodec#apply(PredicateCodecBuilder)} and {@link PredicateCodecBuilder#build(MethodHandle)}.
		 * <h2>Example</h2>
		 * {@snippet lang=java :
		 *
		 * }
		 */
		public abstract MapCodec<T> getCodec();
	}
}
