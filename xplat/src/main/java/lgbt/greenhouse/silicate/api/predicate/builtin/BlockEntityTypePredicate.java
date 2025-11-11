package lgbt.greenhouse.silicate.api.predicate.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;

public record BlockEntityTypePredicate(
		ParameterKey<BlockEntity> blockEntity,
		Deferred<BlockEntityType<?>> blockEntityType
) implements GamePredicate<BlockEntityTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		BlockEntity blockEntity = ctx.getParameter(this.blockEntity);
		return blockEntity.getType().equals(blockEntityType.get(ctx));
	}

	@Override
	public GamePredicate.Type<BlockEntityTypePredicate> getType() {
		return SilicatePredicateTypes.BLOCK_ENTITY_TYPE;
	}

	public static final class Type extends GamePredicate.Type<BlockEntityTypePredicate> {
		@Override
		protected MapCodec<BlockEntityTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(BlockEntityTypePredicate.class))
					.withParameter(
							"block_entity",
							SilicateValueTypes.BLOCK_ENTITY,
							BlockEntityTypePredicate::blockEntity
					)
					.withValue(
							"block_entity_type",
							SilicateValueTypes.BLOCK_ENTITY_TYPE,
							BlockEntityTypePredicate::blockEntityType
					)
					.build();
		}
	}
}
