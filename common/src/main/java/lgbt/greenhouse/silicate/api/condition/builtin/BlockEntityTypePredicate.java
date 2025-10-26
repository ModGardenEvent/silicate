package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ContextParamType;

public record BlockEntityTypePredicate(
	ContextParamType<BlockEntity> paramType,
	BlockEntityType<?> blockEntityType
) implements TypedGamePredicate<BlockEntityTypePredicate, BlockEntity> {
	public static final MapCodec<BlockEntityTypePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(BlockEntity.class)
				.fieldOf("param_type")
				.forGetter(BlockEntityTypePredicate::paramType),
		ResourceLocation.CODEC
				.fieldOf("block_entity_type")
				.forGetter((t) -> BlockEntityType.getKey(t.blockEntityType()))
	).apply(instance, BlockEntityTypePredicate::of));

	private static BlockEntityTypePredicate of(ContextParamType<BlockEntity> paramType, ResourceLocation blockEntityTypeId) {
		return of(paramType, ResourceKey.create(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), blockEntityTypeId));
	}

	private static BlockEntityTypePredicate of(ContextParamType<BlockEntity> paramType, ResourceKey<BlockEntityType<?>> blockEntityTypeKey) {
		return new BlockEntityTypePredicate(paramType, BuiltInRegistries.BLOCK_ENTITY_TYPE.getValueOrThrow(blockEntityTypeKey));
	}

	@Override
	public boolean test(GameContext context) {
		BlockEntity blockEntity = context.getParam(paramType);
		return blockEntity.getType().equals(blockEntityType);
	}

	@Override
	public MapCodec<BlockEntityTypePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<BlockEntityTypePredicate> getType() {
		return PredicateTypes.BLOCK_ENTITY_TYPE;
	}

	@Override
	public ContextParamType<BlockEntity> getParamType() {
		return paramType;
	}
}
