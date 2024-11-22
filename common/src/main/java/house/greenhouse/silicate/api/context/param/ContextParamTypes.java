package house.greenhouse.silicate.api.context.param;

import house.greenhouse.silicate.api.context.GameContext;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static house.greenhouse.silicate.Silicate.id;

/**
 * Built-in parameters for data stored in {@link GameContext}.
 */
public final class ContextParamTypes {
	public static final ContextParamType<Entity> THIS_ENTITY = create("this_entity");
	public static final ContextParamType<Vec3> ORIGIN = create("origin");
	public static final ContextParamType<BlockState> BLOCK_STATE = create("block_state");
	public static final ContextParamType<BlockEntity> BLOCK_ENTITY = create("block_entity");
	public static final ContextParamType<Unit> UNIT = new ContextParamType<>(id("unit"));
	
	private ContextParamTypes() {}
	
	private static <T> ContextParamType<T> create(String name) {
		return new ContextParamType<>(id(name));
	}
}