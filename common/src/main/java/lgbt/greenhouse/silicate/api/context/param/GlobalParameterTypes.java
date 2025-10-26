package lgbt.greenhouse.silicate.api.context.param;

import lgbt.greenhouse.silicate.api.condition.builtin.EntityPassengerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityProjectileOwnerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityTameOwnerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityVehiclePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
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
public final class GlobalParameterTypes {
	public static final GlobalParameterType<Entity> THIS_ENTITY = register("this_entity", Entity.class);
	/**
	 * A temporary parameter type typically used in {@link EntityPassengerPredicate}.
	 */
	public static final GlobalParameterType<Entity> PASSENGER_ENTITY = register("passenger_entity", Entity.class);
	/**
	 * A temporary parameter type typically used in {@link EntityVehiclePredicate}.
	 */
	public static final GlobalParameterType<Entity> VEHICLE_ENTITY = register("vehicle_entity", Entity.class);
	/**
	 * A temporary parameter type typically used in {@link EntityProjectileOwnerPredicate} and {@link EntityTameOwnerPredicate}.
	 */
	public static final GlobalParameterType<Entity> OWNER_ENTITY = register("owner_entity", Entity.class);
	/**
	 * The aggressor in an attacker-victim scenario.
	 */
	public static final GlobalParameterType<Entity> ATTACKING_ENTITY = register("attacking_entity", Entity.class);
	/**
	 * The victim in an attacker-victim scenario.
	 */
	public static final GlobalParameterType<Entity> VICTIM_ENTITY = register("victim_entity", Entity.class);
	public static final GlobalParameterType<Vec3> ORIGIN = register("origin", Vec3.class);
	public static final GlobalParameterType<BlockState> BLOCK_STATE = register("block_state", BlockState.class);
	public static final GlobalParameterType<BlockEntity> BLOCK_ENTITY = register("block_entity", BlockEntity.class);
	public static final GlobalParameterType<Unit> UNIT = new GlobalParameterType<>(id("unit"), Unit.class);

	private GlobalParameterTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	private static <T> GlobalParameterType<T> register(String name, Class<T> clazz) {
		return Registry.register(
				SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE,
				id(name),
				new GlobalParameterType<>(id(name), clazz)
		);
	}
}
