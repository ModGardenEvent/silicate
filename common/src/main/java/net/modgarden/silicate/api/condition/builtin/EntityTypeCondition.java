package net.modgarden.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.SilicateCodecs;
import net.modgarden.silicate.api.condition.GameConditionType;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.condition.TypedGameCondition;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParamType;
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
) implements TypedGameCondition<EntityTypeCondition, Entity> {
	public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(Entity.class)
			.fieldOf("param_type")
			.forGetter(EntityTypeCondition::paramType),
		SilicateCodecs.ENTITY_TYPE_HOLDER_SET
			.fieldOf("entity_type")
			.forGetter(EntityTypeCondition::entityTypes)
	).apply(instance, EntityTypeCondition::new));

	public static EntityTypeCondition of(
		ContextParamType<Entity> paramType,
		EntityType<?> entityType
	) {
		//noinspection deprecation
		return new EntityTypeCondition(
			paramType,
			HolderSet.direct(entityType.builtInRegistryHolder())
		);
	}

	public static EntityTypeCondition of(
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

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
