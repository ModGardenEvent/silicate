package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.level.block.state.BlockState;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;

public record BlockStatePredicate(
	ParameterKey<BlockState> left,
	BlockState right
) implements GamePredicate<BlockStatePredicate> {
	@Override
	public boolean test(GameContext context) {
		BlockState state = context.getParam(left);
		return state.equals(right);
	}

	@Override
	public GamePredicate.Type<BlockStatePredicate> getType() {
		return SilicatePredicateTypes.BLOCK_STATE;
	}

	public static final class Type extends GamePredicate.Type<BlockStatePredicate> {
		@Override
		protected MapCodec<BlockStatePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(BlockStatePredicate.class))
					.withParameter(
							"left",
							SilicateValueTypes.BLOCK_STATE,
							BlockStatePredicate::left
					)
					.withValue(
							"right",
							SilicateValueTypes.BLOCK_STATE,
							BlockStatePredicate::right
					)
					.build();
		}
	}
}
