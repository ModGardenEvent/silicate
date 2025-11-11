package lgbt.greenhouse.silicate.api;

import lgbt.greenhouse.silicate.api.type.ValueType;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKey;

/**
 * {@link ResourceKey}s of registries used in Silicate.
 */
public final class SilicateRegistries {
	public static final ResourceKey<Registry<GamePredicate.Type<?>>> PREDICATE = create("predicate");
	public static final ResourceKey<Registry<GlobalParameterKey<?>>> GLOBAL_PARAMETER_KEY = create("context_param_type");
	public static final ResourceKey<Registry<GamePredicate<?>>> CONDITION = create("condition");
	public static final ResourceKey<Registry<ValueType<?>>> VALUE_TYPE = create("type");

	private static <T> ResourceKey<Registry<T>> create(String name) {
		return ResourceKey.createRegistryKey(SilicateConstants.id(name));
	}
}
