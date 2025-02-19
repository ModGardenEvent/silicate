package net.modgarden.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.modgarden.silicate.api.condition.GameConditionType;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.condition.TypedGameCondition;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParamMap;
import net.modgarden.silicate.api.context.param.ContextParamType;

/**
 * A condition that tests {@link #condition} with the owner of {@link #paramType}.
 * Always returns false if the {@link #paramType} has no owner.
 * @param paramType The parameter type that has a passenger.
 * @param condition The game condition to check against.
 */
public record EntityTameOwnerCondition(
		ContextParamType<Entity> paramType,
		TypedGameCondition<?, Entity> condition
) implements TypedGameCondition<EntityTameOwnerCondition, Entity> {
	public static final MapCodec<EntityTameOwnerCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.getCodec(Entity.class)
					.fieldOf("param_type")
					.forGetter(EntityTameOwnerCondition::paramType),
			TypedGameCondition.getTypedCodec(Entity.class)
					.fieldOf("condition")
					.forGetter(EntityTameOwnerCondition::condition)
	).apply(instance, EntityTameOwnerCondition::of));

	private static EntityTameOwnerCondition of(ContextParamType<Entity> paramType, TypedGameCondition<?, Entity> condition) {
		return new EntityTameOwnerCondition(
				paramType,
				condition
		);
	}

	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(paramType);

		if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
			ContextParamMap oldParamMap = oldContext.getParams();
			ContextParamMap.Mutable paramMap = ContextParamMap.Mutable.of(oldParamMap);
			return testOwner(oldContext, ownable.getOwner(), paramMap);
		}
		return false;
	}

	private boolean testOwner(GameContext oldContext, Entity owner, ContextParamMap.Mutable paramMap) {
		paramMap.set(condition.getParamType(), owner);
		GameContext context = GameContext.of(oldContext.getLevel(), paramMap);
		return condition.test(context);
	}

	@Override
	public MapCodec<EntityTameOwnerCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<EntityTameOwnerCondition> getType() {
		return GameConditionTypes.ENTITY_TAME_OWNER;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
