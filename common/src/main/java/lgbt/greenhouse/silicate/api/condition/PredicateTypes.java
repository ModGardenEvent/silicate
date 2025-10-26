package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.builtin.*;
import net.minecraft.core.Registry;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.id;

/**
 * Built-in {@link PredicateType}s.
 */
public final class PredicateTypes {
	public static final PredicateType<CompoundPredicate> COMPOUND = register("compound", CompoundPredicate.CODEC);
	public static final PredicateType<InvertedPredicate> INVERTED = register("inverted", InvertedPredicate.CODEC);
	public static final PredicateType<RetypedPredicate> RETYPED = register("retyped", RetypedPredicate.CODEC);
	public static final PredicateType<AlwaysPredicate> ALWAYS = register("always", AlwaysPredicate.CODEC);
	public static final PredicateType<EntityTypePredicate> ENTITY_TYPE = register("entity_type", EntityTypePredicate.CODEC);
	public static final PredicateType<EntityPassengerPredicate> ENTITY_PASSENGER = register("entity_passenger", EntityPassengerPredicate.CODEC);
	public static final PredicateType<EntityProjectileOwnerPredicate> ENTITY_PROJECTILE_OWNER = register("entity_projectile_owner", EntityProjectileOwnerPredicate.CODEC);
	public static final PredicateType<EntityTameOwnerPredicate> ENTITY_TAME_OWNER = register("entity_tame_owner", EntityTameOwnerPredicate.CODEC);
	public static final PredicateType<EntityVehiclePredicate> ENTITY_VEHICLE = register("entity_vehicle", EntityVehiclePredicate.CODEC);
	public static final PredicateType<BlockStatePredicate> BLOCK_STATE = register("block_state", BlockStatePredicate.CODEC);
	public static final PredicateType<Vec3Predicate> VEC3 = register("vec3", Vec3Predicate.CODEC);
	public static final PredicateType<BlockEntityTypePredicate> BLOCK_ENTITY_TYPE = register("block_entity_type", BlockEntityTypePredicate.CODEC);
	public static final PredicateType<PlayerGameTypePredicate> PLAYER_GAME_TYPE = register("player_game_type", PlayerGameTypePredicate.CODEC);

	private PredicateTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	private static <T extends GamePredicate<T>> PredicateType<T> register(
			String name,
			MapCodec<T> codec
	) {
		return Registry.register(
			SilicateBuiltInRegistries.PREDICATE,
			id(name),
			new PredicateType<>(codec)
		);
	}
}
