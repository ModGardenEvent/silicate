package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.GameConditionType;
import lgbt.greenhouse.silicate.api.condition.GameConditionTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGameCondition;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ContextParamMap;
import lgbt.greenhouse.silicate.api.context.param.ContextParamType;
import lgbt.greenhouse.silicate.api.context.param.ContextParamTypes;

/**
 * A condition that tests {@link #condition} with the owner of {@link #paramType}.
 * Always returns false if the {@link #paramType} has no owner.
 * @param paramType The parameter type that has a passenger.
 * @param condition The game condition to check against.
 */
public record EntityProjectileOwnerCondition(
		ContextParamType<Entity> paramType,
		Holder<TypedGameCondition<?, Entity>> condition
) implements TypedGameCondition<EntityProjectileOwnerCondition, Entity> {
	public static final MapCodec<EntityProjectileOwnerCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.getCodec(Entity.class)
					.fieldOf("param_type")
					.forGetter(EntityProjectileOwnerCondition::paramType),
			TypedGameCondition.getTypedCodec(Entity.class)
					.fieldOf("condition")
					.forGetter(EntityProjectileOwnerCondition::condition)
	).apply(instance, EntityProjectileOwnerCondition::of));

	private static EntityProjectileOwnerCondition of(ContextParamType<Entity> paramType, Holder<TypedGameCondition<?, Entity>> condition) {
		return new EntityProjectileOwnerCondition(
				paramType,
				condition
		);
	}

	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(paramType);

		if (entity instanceof TraceableEntity traceable && traceable.getOwner() != null) {
			ContextParamMap oldParamMap = oldContext.getParams();
			ContextParamMap.Mutable paramMap = ContextParamMap.Mutable.of(oldParamMap);
			return testOwner(oldContext.getLevel(), traceable.getOwner(), paramMap);
		}
		return false;
	}

	private boolean testOwner(Level level, Entity owner, ContextParamMap.Mutable paramMap) {
		paramMap.set(ContextParamTypes.OWNER_ENTITY, owner);
		GameContext context = GameContext.of(level, paramMap);
		return condition.value().test(context);
	}

	@Override
	public MapCodec<EntityProjectileOwnerCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<EntityProjectileOwnerCondition> getType() {
		return GameConditionTypes.ENTITY_PROJECTILE_OWNER;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
