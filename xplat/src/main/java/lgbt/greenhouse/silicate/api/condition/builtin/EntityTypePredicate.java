package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * A predicate to check an entity's {@link EntityType}.
 */
public record EntityTypePredicate(
	ParameterKey<Entity> entity,
	HolderSet<EntityType<?>> entityTypes
) implements GamePredicate<EntityTypePredicate> {
	public static EntityTypePredicate of(
		ParameterKey<Entity> paramKey,
		EntityType<?> entityType
	) {
		//noinspection deprecation
		return new EntityTypePredicate(
			paramKey,
			HolderSet.direct(entityType.builtInRegistryHolder())
		);
	}

	public static EntityTypePredicate of(
		ParameterKey<Entity> paramKey,
		TagKey<EntityType<?>> entityTag
	) {
		return new EntityTypePredicate(
			paramKey,
			BuiltInRegistries.ENTITY_TYPE.getOrThrow(entityTag)
		);
	}

	@Override
	public boolean test(GameContext context) {
		return context
			.getParam(this.entity)
			.getType()
			.is(this.entityTypes);
	}

	@Override
	public GamePredicate.Type<EntityTypePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_TYPE;
	}

	public static class Type extends GamePredicate.Type<EntityTypePredicate> {
		@Override
		protected MapCodec<EntityTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityTypePredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityTypePredicate::entity
					)
					.withValue(
							"entity_type",
							SilicateValueTypes.HOLDER_SET_ENTITY_TYPE,
							EntityTypePredicate::entityTypes
					)
					.build();
		}
	}
}
