package house.greenhouse.silicate.api;

import com.mojang.serialization.Lifecycle;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static house.greenhouse.silicate.Silicate.id;

/**
 * Built-in Registries for Silicate.
 */
public final class SilicateRegistries {
	public static final Registry<GameConditionType<?>> GAME_CONDITION_TYPE = create("game_condition_type");
	public static final Registry<ContextParamType<?>> CONTEXT_PARAM_TYPE = create("context_param_type");
	
	private SilicateRegistries() {}
	
	private static <T> Registry<T> create(String name) {
		return new MappedRegistry<>(
				ResourceKey.createRegistryKey(id(name)),
				Lifecycle.stable(),
				false
		);
	}
}