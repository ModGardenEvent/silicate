package net.modgarden.silicate.api;

import com.mojang.serialization.Lifecycle;
import net.modgarden.silicate.api.condition.GameConditionType;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.context.param.ContextParamType;
import net.modgarden.silicate.api.context.param.ContextParamTypes;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import static net.modgarden.silicate.Silicate.id;

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

	private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
		return new MappedRegistry<>(
				key,
				Lifecycle.stable(),
				false
		);
	}
}
