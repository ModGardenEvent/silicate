package lgbt.greenhouse.silicate.api;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterTypes;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

import static lgbt.greenhouse.silicate.Silicate.id;

/**
 * Built-in Registries for Silicate.
 */
public final class SilicateBuiltInRegistries {
	public static final Registry<PredicateType<?>> PREDICATE = create(SilicateRegistries.PREDICATE);
	public static final Registry<GlobalParameterType<?>> CONTEXT_PARAM_TYPE = create(SilicateRegistries.CONTEXT_PARAM_TYPE);

	private SilicateBuiltInRegistries() {}

	@ApiStatus.Internal
	public static void registerAll() {
		registerRegistries();
		PredicateTypes.registerAll();
		GlobalParameterTypes.registerAll();
	}

	@SuppressWarnings("unchecked")
	private static void registerRegistries() {
		Registry<Registry<?>> registryRegistry = (Registry<Registry<?>>) BuiltInRegistries.REGISTRY;
		Registry.register(registryRegistry, id("context_param_type"), CONTEXT_PARAM_TYPE);
		Registry.register(registryRegistry, id("predicate"), PREDICATE);
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
