package house.greenhouse.silicate.api.context.param;

import house.greenhouse.silicate.api.SilicateRegistries;
import house.greenhouse.silicate.api.condition.builtin.EntityPassengerCondition;
import house.greenhouse.silicate.api.condition.builtin.EntityVehicleCondition;
import house.greenhouse.silicate.api.context.GameContext;
import net.minecraft.core.Registry;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import static house.greenhouse.silicate.Silicate.id;

/**
 * Built-in parameters for data stored in {@link GameContext}.
 */
public final class ContextParamTypes {
	public static final ContextParamType<Entity> THIS_ENTITY = register("this_entity", Entity.class);
	/**
	 * A temporary parameter type typically used in {@link EntityPassengerCondition}.
	 */
	public static final ContextParamType<Entity> PASSENGER_ENTITY = register("passenger_entity", Entity.class);
	/**
	 * A temporary parameter type typically used in {@link EntityVehicleCondition}.
	 */
	public static final ContextParamType<Entity> VEHICLE_ENTITY = register("vehicle_entity", Entity.class);
	/**
	 * The aggressor in an attacker-victim scenario.
	 */
	public static final ContextParamType<Entity> ATTACKING_ENTITY = register("attacking_entity", Entity.class);
	/**
	 * The victim in an attacker-victim scenario.
	 */
	public static final ContextParamType<Entity> VICTIM_ENTITY = register("victim_entity", Entity.class);
	public static final ContextParamType<Vec3> ORIGIN = register("origin", Vec3.class);
	public static final ContextParamType<BlockState> BLOCK_STATE = register("block_state", BlockState.class);
	public static final ContextParamType<BlockEntity> BLOCK_ENTITY = register("block_entity", BlockEntity.class);
	public static final ContextParamType<Unit> UNIT = new ContextParamType<>(id("unit"), Unit.class);
	
	private ContextParamTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}
	
	private static <T> ContextParamType<T> register(String name, Class<T> clazz) {
		return Registry.register(
				SilicateRegistries.CONTEXT_PARAM_TYPE,
				id(name),
				new ContextParamType<>(id(name), clazz)
		);
	}
}