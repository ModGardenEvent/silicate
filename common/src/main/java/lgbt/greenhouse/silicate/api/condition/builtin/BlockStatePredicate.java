package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.level.block.state.BlockState;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.NotNull;

public record BlockStatePredicate(
	GlobalParameterKey<BlockState> paramType,
	BlockState blockState
) implements TypedGamePredicate<BlockStatePredicate, BlockState> {
	public static final MapCodec<BlockStatePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GlobalParameterKey.getCodec(SilicateValueTypes.BLOCK_STATE)
			.fieldOf("param_type")
			.forGetter(BlockStatePredicate::paramType),
		BlockState.CODEC
			.fieldOf("block_state")
			.forGetter(BlockStatePredicate::blockState)
	).apply(instance, BlockStatePredicate::new));

	@Override
	public boolean test(GameContext context) {
		BlockState state = context.getParam(paramType);
		return state.equals(blockState);
	}

	@Override
	public @NotNull MapCodec<BlockStatePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Type<BlockStatePredicate> getType() {
		return SilicatePredicateTypes.BLOCK_STATE;
	}

	@Override
	public GlobalParameterKey<BlockState> getParamType() {
		return paramType;
	}
}
