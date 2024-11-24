package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record BlockEntityTypeCondition(
	ContextParamType<BlockEntity> paramType,
	BlockEntityType<?> blockEntityType
) implements TypedGameCondition<BlockEntityTypeCondition, BlockEntity> {
	public static final MapCodec<BlockEntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(BlockEntity.class)
				.fieldOf("param_type")
				.forGetter(BlockEntityTypeCondition::paramType),
		ResourceLocation.CODEC
				.fieldOf("block_entity_type")
				.forGetter((t) -> BlockEntityType.getKey(t.blockEntityType()))
	).apply(instance, BlockEntityTypeCondition::of));
	
	private static BlockEntityTypeCondition of(ContextParamType<BlockEntity> paramType, ResourceLocation blockEntityTypeId) {
		return new BlockEntityTypeCondition(paramType, BuiltInRegistries.BLOCK_ENTITY_TYPE.get(blockEntityTypeId));
	}
	
	@Override
	public boolean test(GameContext context) {
		BlockEntity blockEntity = context.getParam(paramType);
		return blockEntity.getType().equals(blockEntityType);
	}
	
	@Override
	public MapCodec<BlockEntityTypeCondition> getCodec() {
		return CODEC;
	}
	
	@Override
	public GameConditionType<BlockEntityTypeCondition> getType() {
		return GameConditionTypes.BLOCK_ENTITY_TYPE;
	}

	@Override
	public ContextParamType<BlockEntity> getParamType() {
		return paramType;
	}
}