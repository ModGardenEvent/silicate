package lgbt.greenhouse.silicate.api.predicate;

import lgbt.greenhouse.silicate.api.predicate.minecraft.*;
import lgbt.greenhouse.silicate.api.predicate.std.*;
import net.minecraft.core.Registry;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static lgbt.greenhouse.silicate.impl.SilicateConstants.id;

/**
 * Built-in {@link GamePredicate.Type}s.
 */
public final class SilicatePredicateTypes {
	// Standard
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
	public static final GamePredicate.Type<EqualsPredicate> EQUALS = register(
			"equals",
			EqualsPredicate.Type::new
	);
	public static final GamePredicate.Type<IsTypePredicate> IS_TYPE = register(
			"is_type",
			IsTypePredicate.Type::new
	);
	// Minecraft
	public static final GamePredicate.Type<EntityIsTypePredicate> ENTITY_IS_TYPE = register(
			"entity_is_type",
			EntityIsTypePredicate.Type::new
	);
	public static final GamePredicate.Type<EntityHasPassengerPredicate> ENTITY_HAS_PASSENGER = register(
			"entity_has_passenger",
			EntityHasPassengerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityProjectileHasOwnerPredicate> ENTITY_PROJECTILE_HAS_OWNER = register(
			"entity_projectile_has_owner",
			EntityProjectileHasOwnerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityHasTameOwnerPredicate> ENTITY_HAS_TAME_OWNER = register(
			"entity_has_tame_owner",
			EntityHasTameOwnerPredicate.Type::new
	);
	public static final GamePredicate.Type<EntityHasVehiclePredicate> ENTITY_HAS_VEHICLE = register(
			"entity_has_vehicle",
			EntityHasVehiclePredicate.Type::new
	);
	public static final GamePredicate.Type<Vec3Predicate> VEC3 = register(
			"vec3",
			Vec3Predicate.Type::new
	);
	public static final GamePredicate.Type<BlockEntityIsTypePredicate> BLOCK_ENTITY_IS_TYPE = register(
			"block_entity_is_type",
			BlockEntityIsTypePredicate.Type::new
	);
	public static final GamePredicate.Type<PlayerIsGameTypePredicate> PLAYER_IS_GAME_TYPE = register(
			"player_is_game_type",
			PlayerIsGameTypePredicate.Type::new
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
