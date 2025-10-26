package lgbt.greenhouse.silicate.api.type;

import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Built-in types in Silicate.
 */
public final class SilicateValueTypes {
	public static final ValueType<BlockEntity> BLOCK_ENTITY = register("block_entity", BlockEntity.class);
	public static final ValueType<BlockState> BLOCK_STATE = register("block_state", BlockState.class);
	public static final ValueType<Entity> ENTITY = register("entity", Entity.class);
	public static final ValueType<Mob> MOB = register("mob", Mob.class);
	public static final ValueType<Monster> HOSTILE_MOB = register("hostile_mob", Monster.class);
	public static final ValueType<NeutralMob> NEUTRAL_MOB = register("neutral_mob", NeutralMob.class);
	public static final ValueType<AgeableMob> PASSIVE_MOB = register("passive_mob", AgeableMob.class);
	public static final ValueType<Vec3> VEC3 = register("vec3", Vec3.class);

	private SilicateValueTypes() {}

	private static <T> ValueType<T> register(String name, Class<T> clazz) {
		return Registry.register(SilicateBuiltInRegistries.VALUE_TYPE, Silicate.id(name), new ValueType<>(clazz));
	}
}
