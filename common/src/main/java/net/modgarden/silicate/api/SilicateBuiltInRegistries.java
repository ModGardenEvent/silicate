package net.modgarden.silicate.api;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.modgarden.silicate.Silicate;
import net.modgarden.silicate.api.condition.GameConditionType;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.context.param.ContextParamType;
import net.modgarden.silicate.api.context.param.ContextParamTypes;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * Built-in Registries for Silicate.
 */
public final class SilicateBuiltInRegistries {
	public static final Registry<GameConditionType<?>> GAME_CONDITION_TYPE = create(SilicateRegistries.GAME_CONDITION_TYPE);
	public static final Registry<ContextParamType<?>> CONTEXT_PARAM_TYPE = create(SilicateRegistries.CONTEXT_PARAM_TYPE);

	private SilicateBuiltInRegistries() {}

	@ApiStatus.Internal
	public static void registerAll() {
		GameConditionTypes.registerAll();
		ContextParamTypes.registerAll();
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
