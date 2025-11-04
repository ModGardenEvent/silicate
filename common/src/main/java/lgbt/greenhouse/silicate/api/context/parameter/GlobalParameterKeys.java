package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.condition.builtin.EntityPassengerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityProjectileOwnerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityTameOwnerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityVehiclePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.core.Registry;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.id;

/**
 * Built-in parameters for data stored in {@link GameContext}.
 */
public final class GlobalParameterKeys {
	public static final GlobalParameterKey<Entity> THIS_ENTITY = register("this_entity", SilicateValueTypes.ENTITY);
	/**
	 * A temporary parameter type typically used in {@link EntityPassengerPredicate}.
	 */
	public static final GlobalParameterKey<Entity> PASSENGER_ENTITY = register("passenger_entity", SilicateValueTypes.ENTITY);
	/**
	 * A temporary parameter type typically used in {@link EntityVehiclePredicate}.
	 */
	public static final GlobalParameterKey<Entity> VEHICLE_ENTITY = register("vehicle_entity", SilicateValueTypes.ENTITY);
	/**
	 * A temporary parameter type typically used in {@link EntityProjectileOwnerPredicate} and {@link EntityTameOwnerPredicate}.
	 */
	public static final GlobalParameterKey<Entity> OWNER_ENTITY = register("owner_entity", SilicateValueTypes.ENTITY);
	/**
	 * The aggressor in an attacker-victim scenario.
	 */
	public static final GlobalParameterKey<Entity> ATTACKING_ENTITY = register("attacking_entity", SilicateValueTypes.ENTITY);
	/**
	 * The victim in an attacker-victim scenario.
	 */
	public static final GlobalParameterKey<Entity> VICTIM_ENTITY = register("victim_entity", SilicateValueTypes.ENTITY);
	public static final GlobalParameterKey<Vec3> ORIGIN = register("origin", SilicateValueTypes.VEC3);
	public static final GlobalParameterKey<BlockState> BLOCK_STATE = register("block_state", SilicateValueTypes.BLOCK_STATE);
	public static final GlobalParameterKey<BlockEntity> BLOCK_ENTITY = register("block_entity", SilicateValueTypes.BLOCK_ENTITY);
	public static final GlobalParameterKey<Unit> UNIT = new GlobalParameterKey<>(id("unit"), SilicatePrimitives.UNIT);

	private GlobalParameterKeys() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	private static <T> GlobalParameterKey<T> register(String name, ValueType<T> type) {
		return Registry.register(
				SilicateBuiltInRegistries.GLOBAL_PARAMETER_KEY,
				id(name),
				new GlobalParameterKey<>(id(name), type)
		);
	}
}
