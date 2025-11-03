package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.phys.Vec3;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the caller to check if the value of a {@link Vec3} passes equality/inequality comparisons with {@link #right}.
 * @param left The left operand.
 * @param right The {@link Vec3} to do operations on (to the right).
 */
public record Vec3Predicate(
	ParameterKey<Vec3> left,
	Vec3Comparison comparison,
	Vec3 right
) implements TypedGamePredicate<Vec3Predicate, Vec3> {
	@Override
	public boolean test(GameContext context) {
		Vec3 formerOperand = context.getParam(left);
		return comparison.compare(formerOperand, right);
	}

	@Override
	public @NotNull GamePredicate.Type<Vec3Predicate> getType() {
		return SilicatePredicateTypes.VEC3;
	}

	public static final class Type extends GamePredicate.Type<Vec3Predicate> {
		@Override
		protected MapCodec<Vec3Predicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(Vec3Predicate.class))
					.withField(
							"left",
							SilicateValueTypes.VEC3
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
