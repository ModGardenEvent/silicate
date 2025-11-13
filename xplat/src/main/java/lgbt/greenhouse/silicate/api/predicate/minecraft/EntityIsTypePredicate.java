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
public record EntityIsTypePredicate(
		ParameterKey<Entity> entity,
		Deferred<HolderSet<EntityType<?>>> entityTypes
) implements GamePredicate<EntityIsTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return ctx
			.getParameter(this.entity)
			.getType()
			.is(this.entityTypes.get(ctx));
	}

	@Override
	public GamePredicate.Type<EntityIsTypePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_IS_TYPE;
	}

	public static class Type extends GamePredicate.Type<EntityIsTypePredicate> {
		@Override
		protected MapCodec<EntityIsTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityIsTypePredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityIsTypePredicate::entity
					)
					.withValue(
							"entity_type",
							SilicateValueTypes.HOLDER_SET_ENTITY_TYPE,
							EntityIsTypePredicate::entityTypes
					)
					.build();
		}
	}
}
