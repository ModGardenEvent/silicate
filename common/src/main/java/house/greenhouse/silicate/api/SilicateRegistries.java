package house.greenhouse.silicate.api;

import com.mojang.serialization.Lifecycle;
import house.greenhouse.silicate.api.condition.GameConditionType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static house.greenhouse.silicate.Silicate.id;

/**
 * Built-in Registries for Silicate.
 */
public final class SilicateRegistries {
	private static final ResourceKey<Registry<GameConditionType<?>>> GAME_CONDITION_TYPE_KEY = ResourceKey.createRegistryKey(id("game_condition_type"));
	public static final Registry<GameConditionType<?>> GAME_CONDITION_TYPE = new MappedRegistry<>(
		GAME_CONDITION_TYPE_KEY,
		Lifecycle.stable(),
		false
	);
	
	private SilicateRegistries() {}
}