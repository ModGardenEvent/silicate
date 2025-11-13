package lgbt.greenhouse.silicate.api.predicate.minecraft;

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

public record BlockEntityIsTypePredicate(
		ParameterKey<BlockEntity> blockEntity,
		Deferred<BlockEntityType<?>> blockEntityType
) implements GamePredicate<BlockEntityIsTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		BlockEntity blockEntity = ctx.getParameter(this.blockEntity);
		return blockEntity.getType().equals(blockEntityType.get(ctx));
	}

	@Override
	public GamePredicate.Type<BlockEntityIsTypePredicate> getType() {
		return SilicatePredicateTypes.BLOCK_ENTITY_IS_TYPE;
	}

	public static final class Type extends GamePredicate.Type<BlockEntityIsTypePredicate> {
		@Override
		protected MapCodec<BlockEntityIsTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(BlockEntityIsTypePredicate.class))
					.withParameter(
							"block_entity",
							SilicateValueTypes.BLOCK_ENTITY,
							BlockEntityIsTypePredicate::blockEntity
					)
					.withValue(
							"block_entity_type",
							SilicateValueTypes.BLOCK_ENTITY_TYPE,
							BlockEntityIsTypePredicate::blockEntityType
					)
					.build();
		}
	}
}
