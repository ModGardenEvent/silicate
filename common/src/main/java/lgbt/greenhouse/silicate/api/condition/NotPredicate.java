package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate that inverts another condition's test.
 * @param condition The condition to invert.
 */
public record NotPredicate(
		Holder<GamePredicate<?>> condition
) implements GamePredicate<NotPredicate> {
	@Override
	public boolean test(GameContext context) {
		return !condition.value().test(context);
	}

	@Override
	public @NotNull GamePredicate.Type<NotPredicate> getType() {
		return SilicatePredicateTypes.NOT;
	}

	public static final class Type extends GamePredicate.Type<NotPredicate> {
		@Override
		public MapCodec<NotPredicate> getCodec() {
			return createBaseCodec()
					.apply(PredicateCodecBuilder.of(NotPredicate.class))
					.withField(
							"condition",
							SilicateValueTypes.CONDITION,
					);
		}
	}
}
