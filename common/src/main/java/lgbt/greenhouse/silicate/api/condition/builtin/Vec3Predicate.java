package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;

/**
 * Allows the caller to check if the value of a {@link Vec3} passes equality/inequality comparisons with {@link #latterOperand}.
 * @param paramType The parameter type.
 * @param latterOperand The {@link Vec3} to do operations on (to the right).
 */
public record Vec3Predicate(
	GlobalParameterType<Vec3> paramType,
	Vec3Comparison comparison,
	Vec3 latterOperand
) implements TypedGamePredicate<Vec3Predicate, Vec3> {
	public static final MapCodec<Vec3Predicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GlobalParameterType.getCodec(Vec3.class)
			.fieldOf("param_type")
			.forGetter(Vec3Predicate::paramType),
		Vec3Comparison.CODEC
			.fieldOf("comparison")
			.forGetter(Vec3Predicate::comparison),
		Vec3.CODEC
			.fieldOf("latter_operand")
			.forGetter(Vec3Predicate::latterOperand)
	).apply(instance, Vec3Predicate::new));

	@Override
	public boolean test(GameContext context) {
		Vec3 formerOperand = context.getParam(paramType);
		return comparison.compare(formerOperand, latterOperand);
	}

	@Override
	public MapCodec<Vec3Predicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<Vec3Predicate> getType() {
		return PredicateTypes.VEC3;
	}

	@Override
	public GlobalParameterType<Vec3> getParamType() {
		return paramType;
	}
}
