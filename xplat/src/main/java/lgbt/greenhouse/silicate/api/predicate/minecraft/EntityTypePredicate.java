package lgbt.greenhouse.silicate.api.predicate.minecraft;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * A predicate to check an entity's {@link EntityType}.
 */
public record EntityTypePredicate(
		ParameterKey<Entity> entity,
		Deferred<HolderSet<EntityType<?>>> entityTypes
) implements GamePredicate<EntityTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return ctx
			.getParameter(this.entity)
			.getType()
			.is(this.entityTypes.get(ctx));
	}

	@Override
	public GamePredicate.Type<EntityTypePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_IS_TYPE;
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
