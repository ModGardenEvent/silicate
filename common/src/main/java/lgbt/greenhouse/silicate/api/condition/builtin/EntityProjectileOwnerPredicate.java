package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate that tests {@link #condition} with the owner of {@link #paramType}.
 * Always returns false if the {@link #paramType} has no owner.
 * @param paramType The parameter type that has a passenger.
 * @param condition The game condition to check against.
 */
public record EntityProjectileOwnerPredicate(
		GlobalParameterKey<Entity> paramType,
		Holder<TypedGamePredicate<?, Entity>> condition
) implements TypedGamePredicate<EntityProjectileOwnerPredicate, Entity> {
	public static final MapCodec<EntityProjectileOwnerPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GlobalParameterKey.getCodec(SilicateValueTypes.ENTITY)
					.fieldOf("param_type")
					.forGetter(EntityProjectileOwnerPredicate::paramType),
			TypedGamePredicate.getTypedCodec(SilicateValueTypes.ENTITY)
					.fieldOf("condition")
					.forGetter(EntityProjectileOwnerPredicate::condition)
	).apply(instance, EntityProjectileOwnerPredicate::of));

	private static EntityProjectileOwnerPredicate of(GlobalParameterKey<Entity> paramType, Holder<TypedGamePredicate<?, Entity>> condition) {
		return new EntityProjectileOwnerPredicate(
				paramType,
				condition
		);
	}

	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(paramType);

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
	public @NotNull MapCodec<EntityProjectileOwnerPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Type<EntityProjectileOwnerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PROJECTILE_OWNER;
	}

	@Override
	public GlobalParameterKey<Entity> getParamType() {
		return paramType;
	}
}
