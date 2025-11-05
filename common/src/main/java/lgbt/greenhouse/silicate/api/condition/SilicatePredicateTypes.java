package lgbt.greenhouse.silicate.api.condition;

import lgbt.greenhouse.silicate.api.condition.builtin.*;
import net.minecraft.core.Registry;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static lgbt.greenhouse.silicate.impl.Silicate.id;

/**
 * Built-in {@link GamePredicate.Type}s.
 */
public final class SilicatePredicateTypes {
	public static final GamePredicate.Type<AllPredicate> ALL = register(
			"all",
			AllPredicate.Type::new
	);
	public static final GamePredicate.Type<AnyPredicate> ANY = register(
			"any",
			AnyPredicate.Type::new
	);
	public static final GamePredicate.Type<NotPredicate> NOT = register(
			"not",
			NotPredicate.Type::new
	);
	public static final GamePredicate.Type<AlwaysPredicate> ALWAYS = register(
			"always",
			AlwaysPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityTypePredicate> ENTITY_TYPE = register(
			"entity_type",
			EntityTypePredicate.Type::new
	);
	public static final GamePredicate.Type<EntityPassengerPredicate> ENTITY_PASSENGER = register(
			"entity_passenger",
			EntityPassengerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityProjectileOwnerPredicate> ENTITY_PROJECTILE_OWNER = register(
			"entity_projectile_owner",
			EntityProjectileOwnerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityTameOwnerPredicate> ENTITY_TAME_OWNER = register(
			"entity_tame_owner",
			EntityTameOwnerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityVehiclePredicate> ENTITY_VEHICLE = register(
			"entity_vehicle",
			EntityVehiclePredicate.Type::new
	);
	public static final GamePredicate.Type<BlockStatePredicate> BLOCK_STATE = register(
			"block_state",
			BlockStatePredicate.Type::new
	);
	public static final GamePredicate.Type<Vec3Predicate> VEC3 = register(
			"vec3",
			Vec3Predicate.Type::new
	);
	public static final GamePredicate.Type<BlockEntityTypePredicate> BLOCK_ENTITY_TYPE = register(
			"block_entity_type",
			BlockEntityTypePredicate.Type::new
	);
	public static final GamePredicate.Type<PlayerGameTypePredicate> PLAYER_GAME_TYPE = register(
			"player_game_type",
			PlayerGameTypePredicate.Type::new
	);

	private SilicatePredicateTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	private static <T extends GamePredicate<T>> GamePredicate.Type<T> register(
			String name,
			Supplier<GamePredicate.Type<T>> typeSupplier
	) {
		return Registry.register(
			SilicateBuiltInRegistries.PREDICATE,
			id(name),
			typeSupplier.get()
		);
	}
}
