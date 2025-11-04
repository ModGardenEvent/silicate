package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate that tests {@link #condition} with the owner of {@link #entity}.
 * Always returns false if the {@link #entity} has no owner.
 * @param entity The passenger.
 * @param condition The game condition to check against.
 */
public record EntityTameOwnerPredicate(
		ParameterKey<Entity> entity,
		Holder<GamePredicate<?>> condition
) implements GamePredicate<EntityTameOwnerPredicate> {
	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(this.entity);

		if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
			ParameterMap oldParamMap = oldContext.getParams();
			ParameterMap.Mutable paramMap = ParameterMap.Mutable.of(oldParamMap);
			return testOwner(oldContext.getLevel(), ownable.getOwner(), paramMap);
		}
		return false;
	}

	private boolean testOwner(Level level, Entity owner, ParameterMap.Mutable paramMap) {
		paramMap.set(GlobalParameterKeys.OWNER_ENTITY, owner);
		GameContext context = GameContext.of(level, paramMap);
		return condition.value().test(context);
	}

	@Override
	public @NotNull GamePredicate.Type<EntityTameOwnerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_TAME_OWNER;
	}

	public static final class Type extends GamePredicate.Type<EntityTameOwnerPredicate> {
		@Override
		protected MapCodec<EntityTameOwnerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityTameOwnerPredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityTameOwnerPredicate::condition
					)
					.build();
		}
	}
}
