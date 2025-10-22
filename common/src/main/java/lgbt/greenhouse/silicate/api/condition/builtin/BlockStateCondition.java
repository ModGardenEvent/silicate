package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import lgbt.greenhouse.silicate.api.condition.GameConditionType;
import lgbt.greenhouse.silicate.api.condition.GameConditionTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGameCondition;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ContextParamType;

public record BlockStateCondition(
	ContextParamType<BlockState> paramType,
	BlockState blockState
) implements TypedGameCondition<BlockStateCondition, BlockState> {
	public static final MapCodec<BlockStateCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(BlockState.class)
			.fieldOf("param_type")
			.forGetter(BlockStateCondition::paramType),
		BlockState.CODEC
			.fieldOf("block_state")
			.forGetter(BlockStateCondition::blockState)
	).apply(instance, BlockStateCondition::new));

	@Override
	public boolean test(GameContext context) {
		BlockState state = context.getParam(paramType);
		return state.equals(blockState);
	}

	@Override
	public MapCodec<BlockStateCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<BlockStateCondition> getType() {
		return GameConditionTypes.BLOCK_STATE;
	}

	@Override
	public ContextParamType<BlockState> getParamType() {
		return paramType;
	}
}
