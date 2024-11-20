package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import static house.greenhouse.silicate.Silicate.id;

/**
 * A utility class used in codec definition for creating {@link GameCondition}s (subclasses).
 * @param codec The {@link MapCodec} of the {@link GameCondition}, returned by {@link GameCondition#getCodec()}.
 * @param <T> The type of the {@link GameCondition} subclass.
 */
@ApiStatus.Experimental
public record GameConditionType<T extends GameCondition<T>>(MapCodec<T> codec) {
	public static final ResourceKey<Registry<GameConditionType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(id("game_condition_type"));
	public static final Registry<GameConditionType<?>> REGISTRY = new MappedRegistry<>(
			REGISTRY_KEY,
			Lifecycle.stable(),
			false
	);
}
