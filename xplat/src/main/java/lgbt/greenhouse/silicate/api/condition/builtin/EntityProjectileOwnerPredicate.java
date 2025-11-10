package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

/**
 * A predicate that tests {@link #condition} with the owner of {@link #entity}.
 * Always returns false if the {@link #entity} has no owner.
 * @param entity The projectile.
 * @param condition The game condition to check against.
 */
public record EntityProjectileOwnerPredicate(
		ParameterKey<Entity> entity,
		Deferred<Holder<GamePredicate<?>>> condition
) implements GamePredicate<EntityProjectileOwnerPredicate> {
	@Override
	public boolean test(GameContext ctx) {
		Entity entity = ctx.getParam(this.entity);

		if (entity instanceof TraceableEntity traceable && traceable.getOwner() != null) {
			ParameterMap parameterMap = ctx.getParams();
			return testOwner(ctx, traceable.getOwner(), parameterMap);
		}
		return false;
	}

	private boolean testOwner(GameContext ctx, Entity owner, ParameterMap parameterMap) {
		parameterMap.set(GlobalParameterKeys.OWNER_ENTITY, owner);
		GameContext context = GameContext.of(ctx.getLevel(), parameterMap);
		return condition.get(ctx).value().test(context);
	}

	@Override
	public GamePredicate.Type<EntityProjectileOwnerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PROJECTILE_OWNER;
	}

	public static final class Type extends GamePredicate.Type<EntityProjectileOwnerPredicate> {
		@Override
		protected MapCodec<EntityProjectileOwnerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityProjectileOwnerPredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityProjectileOwnerPredicate::entity
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityProjectileOwnerPredicate::condition
					)
					.build();
		}
	}
}
