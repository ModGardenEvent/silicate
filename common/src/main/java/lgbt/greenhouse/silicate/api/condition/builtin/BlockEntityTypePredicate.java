package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.NotNull;

public record BlockEntityTypePredicate(
	ParameterKey<BlockEntity> blockEntity,
	BlockEntityType<?> blockEntityType
) implements TypedGamePredicate<BlockEntityTypePredicate, BlockEntity> {
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
					.withField(
							"block_entity",
							SilicateValueTypes.BLOCK_ENTITY
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
