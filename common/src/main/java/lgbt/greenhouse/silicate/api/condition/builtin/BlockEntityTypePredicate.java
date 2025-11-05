package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.NotNull;

public record BlockEntityTypePredicate(
	ParameterKey<BlockEntity> blockEntity,
	BlockEntityType<?> blockEntityType
) implements GamePredicate<BlockEntityTypePredicate> {
	@Override
	public boolean test(GameContext context) {
		BlockEntity blockEntity = context.getParam(this.blockEntity);
		return blockEntity.getType().equals(blockEntityType);
	}

	@Override
	public @NotNull GamePredicate.Type<BlockEntityTypePredicate> getType() {
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
