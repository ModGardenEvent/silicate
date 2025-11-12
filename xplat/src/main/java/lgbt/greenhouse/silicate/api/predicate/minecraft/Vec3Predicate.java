package lgbt.greenhouse.silicate.api.predicate.minecraft;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.phys.Vec3;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.predicate.minecraft.math.Vec3Comparison;
import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * Allows the caller to check if the value of a {@link Vec3} passes equality/inequality comparisons with {@link #right}.
 * @param left The left operand.
 * @param right The {@link Vec3} to do operations on (to the right).
 */
public record Vec3Predicate(
		ParameterKey<Vec3> left,
		Deferred<Vec3Comparison> comparison,
		Deferred<Vec3> right
) implements GamePredicate<Vec3Predicate> {
	@Override
	public boolean test(GameContext ctx) {
		Vec3 formerOperand = ctx.getParameter(left);
		return comparison.get(ctx).compare(formerOperand, right.get(ctx));
	}

	@Override
	public GamePredicate.Type<Vec3Predicate> getType() {
		return SilicatePredicateTypes.VEC3;
	}

	public static final class Type extends GamePredicate.Type<Vec3Predicate> {
		@Override
		protected MapCodec<Vec3Predicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(Vec3Predicate.class))
					.withParameter(
							"left",
							SilicateValueTypes.VEC3,
							Vec3Predicate::left
					)
					.withValue(
							"comparison",
							SilicateValueTypes.VEC3_COMPARISON,
							Vec3Predicate::comparison
					)
					.withValue(
							"right",
							SilicateValueTypes.VEC3,
							Vec3Predicate::right
					)
					.build();
		}
	}
}
