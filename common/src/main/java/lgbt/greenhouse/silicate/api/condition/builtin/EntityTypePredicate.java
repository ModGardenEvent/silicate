package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import lgbt.greenhouse.silicate.SilicateCodecs;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;

/**
 * A predicate to check an entity's {@link EntityType}.
 */
public record EntityTypePredicate(
	GlobalParameterType<Entity> paramType,
	HolderSet<EntityType<?>> entityTypes
) implements TypedGamePredicate<EntityTypePredicate, Entity> {
	public static final MapCodec<EntityTypePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GlobalParameterType.getCodec(Entity.class)
			.fieldOf("param_type")
			.forGetter(EntityTypePredicate::paramType),
		SilicateCodecs.ENTITY_TYPE_HOLDER_SET
			.fieldOf("entity_type")
			.forGetter(EntityTypePredicate::entityTypes)
	).apply(instance, EntityTypePredicate::new));

	public static EntityTypePredicate of(
		GlobalParameterType<Entity> paramType,
		EntityType<?> entityType
	) {
		//noinspection deprecation
		return new EntityTypePredicate(
			paramType,
			HolderSet.direct(entityType.builtInRegistryHolder())
		);
	}

	public static EntityTypePredicate of(
		GlobalParameterType<Entity> paramType,
		TagKey<EntityType<?>> entityTag
	) {
		return new EntityTypePredicate(
			paramType,
			BuiltInRegistries.ENTITY_TYPE.getOrThrow(entityTag)
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
	public MapCodec<EntityTypePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<EntityTypePredicate> getType() {
		return PredicateTypes.ENTITY_TYPE;
	}

	@Override
	public GlobalParameterType<Entity> getParamType() {
		return paramType;
	}
}
