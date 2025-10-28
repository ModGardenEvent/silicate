package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import lgbt.greenhouse.silicate.SilicateCodecs;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate to check an entity's {@link EntityType}.
 */
public record EntityTypePredicate(
	GlobalParameterKey<Entity> paramType,
	HolderSet<EntityType<?>> entityTypes
) implements TypedGamePredicate<EntityTypePredicate, Entity> {
	public static final MapCodec<EntityTypePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GlobalParameterKey.getCodec(SilicateValueTypes.ENTITY)
			.fieldOf("param_type")
			.forGetter(EntityTypePredicate::paramType),
		SilicateCodecs.ENTITY_TYPE_HOLDER_SET
			.fieldOf("entity_type")
			.forGetter(EntityTypePredicate::entityTypes)
	).apply(instance, EntityTypePredicate::new));

	public static EntityTypePredicate of(
		GlobalParameterKey<Entity> paramType,
		EntityType<?> entityType
	) {
		//noinspection deprecation
		return new EntityTypePredicate(
			paramType,
			HolderSet.direct(entityType.builtInRegistryHolder())
		);
	}

	public static EntityTypePredicate of(
		GlobalParameterKey<Entity> paramType,
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
	public @NotNull MapCodec<EntityTypePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Type<EntityTypePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_TYPE;
	}

	@Override
	public GlobalParameterKey<Entity> getParamType() {
		return paramType;
	}
}
