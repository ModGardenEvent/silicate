package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate that tests {@link #condition} with the owner of {@link #entity}.
 * Always returns false if the {@link #entity} has no owner.
 * @param entity The projectile.
 * @param condition The game condition to check against.
 */
public record EntityProjectileOwnerPredicate(
		ParameterKey<Entity> entity,
		Holder<GamePredicate<?>> condition
) implements GamePredicate<EntityProjectileOwnerPredicate> {
	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(this.entity);

		if (entity instanceof TraceableEntity traceable && traceable.getOwner() != null) {
			ParameterMap oldParamMap = oldContext.getParams();
			ParameterMap.Mutable paramMap = ParameterMap.Mutable.of(oldParamMap);
			return testOwner(oldContext.getLevel(), traceable.getOwner(), paramMap);
		}
		return false;
	}

	private boolean testOwner(Level level, Entity owner, ParameterMap.Mutable paramMap) {
		paramMap.set(GlobalParameterKeys.OWNER_ENTITY, owner);
		GameContext context = GameContext.of(level, paramMap);
		return condition.value().test(context);
	}

	@Override
	public @NotNull GamePredicate.Type<EntityProjectileOwnerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PROJECTILE_OWNER;
	}

	public static final class Type extends GamePredicate.Type<EntityProjectileOwnerPredicate> {
		@Override
		protected MapCodec<EntityProjectileOwnerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityProjectileOwnerPredicate.class))
					.withParameter(
							"player",
							SilicateValueTypes.ENTITY
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
