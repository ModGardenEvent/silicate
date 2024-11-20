package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.condition.GameCondition;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.world.level.block.state.BlockState;

public record BlockStateCondition(
		ContextParamType<BlockState> paramType,
		BlockState blockState
) implements GameCondition<BlockStateCondition> {
	public static final MapCodec<BlockStateCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.<BlockState>getCodec()
					.fieldOf("param_state")
					.forGetter(BlockStateCondition::paramType),
			BlockState.CODEC
					.fieldOf("block_state")
					.forGetter(BlockStateCondition::blockState)
	).apply(instance, BlockStateCondition::new));
	
	@Override
	public boolean test(GameContext context) {
		return context
				.getParam(paramType)
				.equals(blockState);
	}
	
	@Override
	public MapCodec<BlockStateCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<BlockStateCondition> getType() {
		return GameConditionTypes.BLOCK_STATE;
	}
}