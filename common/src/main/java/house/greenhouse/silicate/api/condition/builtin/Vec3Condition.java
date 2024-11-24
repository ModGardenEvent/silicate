package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import house.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.world.phys.Vec3;

/**
 * Allows the caller to check if the value of a {@link Vec3} passes equality/inequality comparisons with {@link #latterOperand}.
 * @param paramType The parameter type.
 * @param latterOperand The {@link Vec3} to do operations on (to the right).
 */
public record Vec3Condition(
	ContextParamType<Vec3> paramType,
	Vec3Comparison comparison,
	Vec3 latterOperand
) implements TypedGameCondition<Vec3Condition, Vec3> {
	public static final MapCodec<Vec3Condition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(Vec3.class)
			.fieldOf("param_type")
			.forGetter(Vec3Condition::paramType),
		Vec3Comparison.CODEC
			.fieldOf("comparison")
			.forGetter(Vec3Condition::comparison),
		Vec3.CODEC
			.fieldOf("latter_operand")
			.forGetter(Vec3Condition::latterOperand)
	).apply(instance, Vec3Condition::new));
	
	@Override
	public boolean test(GameContext context) {
		Vec3 formerOperand = context.getParam(paramType);
		return comparison.compare(formerOperand, latterOperand);
	}
	
	@Override
	public MapCodec<Vec3Condition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<Vec3Condition> getType() {
		return GameConditionTypes.VEC3;
	}

	@Override
	public ContextParamType<Vec3> getParamType() {
		return paramType;
	}
}