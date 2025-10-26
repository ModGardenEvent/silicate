package lgbt.greenhouse.silicate.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.context.param.ContextParamType;

/**
 * {@link ResourceKey}s of registries used in Silicate.
 */
public final class SilicateRegistries {
	public static final ResourceKey<Registry<PredicateType<?>>> PREDICATE = create("predicate");
	public static final ResourceKey<Registry<ContextParamType<?>>> CONTEXT_PARAM_TYPE = create("context_param_type");
	public static final ResourceKey<Registry<GamePredicate<?>>> CONDITION_TEMPLATE = create("condition_template");

	private static <T> ResourceKey<Registry<T>> create(String name) {
		return ResourceKey.createRegistryKey(Silicate.id(name));
	}
}
