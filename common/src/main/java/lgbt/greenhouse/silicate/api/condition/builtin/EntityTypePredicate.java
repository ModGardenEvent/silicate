package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate to check an player's {@link EntityType}.
 */
public record EntityTypePredicate(
	ParameterKey<Entity> paramKey,
	HolderSet<EntityType<?>> entityTypes
) implements TypedGamePredicate<EntityTypePredicate, Entity> {
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
			.getParam(paramKey)
			.getType()
			.is(entityTypes);
	}

	@Override
	public @NotNull GamePredicate.Type<EntityTypePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_TYPE;
	}

	public static class Type extends GamePredicate.Type<EntityTypePredicate> {
		@Override
		protected MapCodec<EntityTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityTypePredicate.class))
					.withParameter(
							"player",
							SilicateValueTypes.ENTITY
					)
					.withValue(
							"entity_types",
							SilicateValueTypes.HOLDER_SET_ENTITY_TYPE,
							EntityTypePredicate::entityTypes
					)
					.build();
		}
	}
}
