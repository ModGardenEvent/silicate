package lgbt.greenhouse.silicate.api.type;

import com.mojang.serialization.*;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import lgbt.greenhouse.silicate.impl.cursed.Clazzy;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Built-in types in Silicate.
 */
public final class SilicateValueTypes {
	public static final ValueType<BlockEntity> BLOCK_ENTITY = register(
			"block_entity",
			BlockEntity.class
	);
	public static final ValueType<BlockEntityType<?>> BLOCK_ENTITY_TYPE = register(
			"block_entity_type",
			fromResourceLocation(
					BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE)
	);
	public static final ValueType<BlockState> BLOCK_STATE = register(
			"block_state",
			BlockState.class,
			BlockState.CODEC
	);
	public static final ValueType<Entity> ENTITY = register("entity", Entity.class);
	public static final ValueType<Player> PLAYER = register("player", Player.class);
	public static final ValueType<Mob> MOB = register("mob", Mob.class);
	public static final ValueType<Monster> HOSTILE_MOB = register(
			"hostile_mob",
			Monster.class
	);
	public static final ValueType<NeutralMob> NEUTRAL_MOB = register(
			"neutral_mob",
			NeutralMob.class
	);
	public static final ValueType<Vec3> VEC3 = register("vec3", Vec3.class, Vec3.CODEC);
	public static final ValueType<Vec3Comparison> VEC3_COMPARISON = register(
			"vec3_comparison",
			Vec3Comparison.class,
			Vec3Comparison.CODEC
	);
	public static final ValueType<Holder<GamePredicate<?>>> CONDITION = register(
			"condition",
			fromHolder(GamePredicate.CODEC)
	);
	public static final ValueType<List<Holder<GamePredicate<?>>>> LIST_CONDITION = register(
			"list_condition",
			fromList(GamePredicate.CODEC)
	);
	public static final ValueType<List<GameType>> LIST_GAME_TYPE = register(
			"list_game_type",
			fromList(GameType.CODEC)
	);
	public static final ValueType<HolderSet<EntityType<?>>> HOLDER_SET_ENTITY_TYPE = register(
			"entity_type",
			fromHolderSet(Registries.ENTITY_TYPE)
	);

	private SilicateValueTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	private static <T> ValueType<T> register(String name, Class<T> clazz, Codec<T> codec) {
		return register(name, new ValueType<>(clazz, codec));
	}

	private static <T> ValueType<T> register(String name, ValueType<T> valueType) {

		return Registry.register(SilicateBuiltInRegistries.VALUE_TYPE, SilicateConstants.id(name), valueType);
	}

	private static <T> ValueType<T> register(String name, Class<T> clazz) {
		return register(name, clazz, null);
	}

	@ApiStatus.Experimental
	public static <T> ValueType<T> fromResourceLocation(Class<? super T> clazz, Registry<T> registry) {
		Codec<T> codec = ResourceLocation.CODEC
				.comapFlatMap(
						id -> {
							try {
								ResourceKey<T> resourceKey = ResourceKey.create(registry.key(), id);
								return DataResult.success(registry.getValueOrThrow(resourceKey));
							} catch (IllegalStateException e) {
								return DataResult.error(e::getMessage);
							}
						},
						registry::getKey
				);
		// This is actually checked because we want people to be able to use generic types
		//noinspection deprecation
		return new ValueType<>(Clazzy.cast(clazz), codec);
	}

	@ApiStatus.Experimental
	public static <T> ValueType<Holder<T>> fromHolder(Codec<Holder<T>> codec) {
		// this is enforced at runtime, and the value is never used
		//noinspection deprecation
		return new ValueType<>(Clazzy.cast(Holder.class), codec);
	}

	@ApiStatus.Experimental
	public static <T> ValueType<HolderSet<T>> fromHolderSet(ResourceKey<Registry<T>> registryKey) {
		var codec = RegistryCodecs.homogeneousList(registryKey);
		// this is enforced at runtime, and the value is never used
		//noinspection deprecation
		return new ValueType<>(Clazzy.cast(HolderSet.class), codec);
	}

	@ApiStatus.Experimental
	public static <T> ValueType<List<T>> fromList(Codec<T> codec) {
		// this is enforced at runtime
		//noinspection deprecation
		return new ValueType<>(Clazzy.cast(List.class), Codec.list(codec));
	}
}
