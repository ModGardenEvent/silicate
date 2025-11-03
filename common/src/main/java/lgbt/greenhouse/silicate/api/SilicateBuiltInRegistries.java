package lgbt.greenhouse.silicate.api;

import com.mojang.serialization.Lifecycle;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

import static lgbt.greenhouse.silicate.Silicate.id;

/**
 * Built-in Registries for Silicate.
 */
public final class SilicateBuiltInRegistries {
	public static final Registry<GamePredicate.Type<?>> PREDICATE = create(SilicateRegistries.PREDICATE);
	public static final Registry<GlobalParameterKey<?>> GLOBAL_PARAMETER_KEY = create(SilicateRegistries.CONTEXT_PARAM_TYPE);
	public static final Registry<ValueType<?>> VALUE_TYPE = create(SilicateRegistries.VALUE_TYPE);

	private SilicateBuiltInRegistries() {}

	@ApiStatus.Internal
	public static void registerAll() {
		registerRegistries();
		SilicateValueTypes.registerAll();
		SilicatePredicateTypes.registerAll();
		GlobalParameterKeys.registerAll();
	}

	@SuppressWarnings("unchecked")
	private static void registerRegistries() {
		Registry<Registry<?>> registryRegistry = (Registry<Registry<?>>) BuiltInRegistries.REGISTRY;
		Registry.register(registryRegistry, id("context_param_type"), GLOBAL_PARAMETER_KEY);
		Registry.register(registryRegistry, id("predicate"), PREDICATE);
		Registry.register(registryRegistry, id("value_type"), VALUE_TYPE);
	}

	/**
	 * Look up a Datapack Registry or else throw.
	 * @param registry The registry's key (found in {@link SilicateRegistries}).
	 * @return The datapack registry.
	 * @param <T> The type of the registry's contents.
	 */
	public static <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<Registry<T>> registry) {
		return getRegistryAccess().orElseThrow().lookupOrThrow(registry);
	}

	/**
	 * Get free {@link RegistryAccess} anywhere you go!
	 */
	private static Optional<RegistryAccess> getRegistryAccess() {
		if (Thread.currentThread().getName().equals("Server thread")) {
			return Optional.of(Silicate.getServer().registryAccess());
		}

		return switch (Silicate.getHelper().getSide()) {
			case CLIENT -> {
				ClientPacketListener connection = Minecraft.getInstance().getConnection();
				if (connection == null) yield Optional.empty();
				yield Optional.of(connection.registryAccess());
			}
			case DEDICATED_SERVER -> Optional.of(Silicate.getServer().registryAccess());
		};
	}

	private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
		return new MappedRegistry<>(
				key,
				Lifecycle.stable(),
				false
		);
	}
}
