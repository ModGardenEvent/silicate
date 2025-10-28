package lgbt.greenhouse.silicate.api.type;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
	public static final ValueType<Vec3> VEC3 = register("vec3", Vec3.class, Vec3.CODEC);
	public static final ValueType<Holder<GamePredicate<?>>> CONDITION = register("condition", fromHolder(GamePredicate.CODEC));
	public static final ValueType<List<Holder<GamePredicate<?>>>> LIST_CONDITION = register("list_condition", fromList(GamePredicate.CODEC));

	private SilicateValueTypes() {}

	private static <T> ValueType<T> register(String name, Class<T> clazz, Codec<T> codec) {
		return register(name, new ValueType<>(clazz, codec));
	}

	private static <T> ValueType<T> register(String name, ValueType<T> valueType) {

		return Registry.register(SilicateBuiltInRegistries.VALUE_TYPE, Silicate.id(name), valueType);
	}

	private static <T> ValueType<T> register(String name, Class<T> clazz) {
		return register(name, clazz, null);
	}

	@SuppressWarnings({ "unchecked", "DataFlowIssue" }) // this is enforced at runtime, and the value is never used
	public static <T> ValueType<Holder<T>> fromHolder(Codec<Holder<T>> codec) {
		return new ValueType<>((Class<Holder<T>>) Holder.<T>direct(null).getClass(), codec);
	}

	@SuppressWarnings("unchecked") // this is enforced at runtime
	public static <T> ValueType<List<T>> fromList(Codec<T> codec) {
		return new ValueType<>((Class<List<T>>) List.<T>of().getClass(), Codec.list(codec));
	}
}
