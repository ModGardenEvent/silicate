package lgbt.greenhouse.silicate.api.predicate.minecraft;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.level.block.state.BlockState;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;

public record BlockStatePredicate(
		ParameterKey<BlockState> left,
		Deferred<BlockState> right
) implements GamePredicate<BlockStatePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		BlockState state = ctx.getParameter(left);
		return state.equals(right.get(ctx));
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
