package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.SilicateCodecs;
import house.greenhouse.silicate.api.condition.GameCondition;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * A condition to check an entity's {@link EntityType}.
 */
public record EntityTypeCondition(
		ContextParamType<Entity> paramType,
		HolderSet<EntityType<?>> entityTypes
) implements GameCondition<EntityTypeCondition> {
	public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.<Entity>getCodec()
					.fieldOf("param_type")
					.forGetter(EntityTypeCondition::paramType),
			SilicateCodecs.ENTITY_TYPE_HOLDER_SET
					.fieldOf("entity_types")
					.forGetter(EntityTypeCondition::entityTypes)
	).apply(instance, EntityTypeCondition::new));
	
	public EntityTypeCondition of(
			ContextParamType<Entity> paramType,
			EntityType<?> entityType
	) {
		//noinspection deprecation
		return new EntityTypeCondition(
				paramType,
				HolderSet.direct(entityType.builtInRegistryHolder())
		);
	}
	
	public EntityTypeCondition of(
			ContextParamType<Entity> paramType,
			TagKey<EntityType<?>> entityTag
	) {
		return new EntityTypeCondition(
				paramType,
				BuiltInRegistries.ENTITY_TYPE.getOrCreateTag(entityTag)
		);
	}
	
	@Override
	public boolean test(GameContext context) {
		return context
				.getParam(paramType)
				.getType()
				.is(entityTypes);
	}
	
	@Override
	public MapCodec<EntityTypeCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<EntityTypeCondition> getType() {
		return GameConditionTypes.ENTITY_TYPE;
	}
}