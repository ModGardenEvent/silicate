package lgbt.greenhouse.silicate.test;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.MOD_ID;

public class SilicateTestGlobalParameterKeys {
	public static final GlobalParameterKey<Projectile> PROJECTILE = register("projectile", SilicateTestValueTypes.PROJECTILE);

	private SilicateTestGlobalParameterKeys() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	@SuppressWarnings("SameParameterValue") // Shush.
	private static <T> GlobalParameterKey<T> register(String name, ValueType<T> type) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID + "_test", name);
		return Registry.register(
				SilicateBuiltInRegistries.GLOBAL_PARAMETER_KEY,
				id,
				new GlobalParameterKey<>(id, type)
		);
	}
}
