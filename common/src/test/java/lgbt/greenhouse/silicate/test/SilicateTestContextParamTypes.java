package lgbt.greenhouse.silicate.test;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.MOD_ID;

public class SilicateTestContextParamTypes {
	public static final GlobalParameterKey<Projectile> PROJECTILE = register("projectile", Projectile.class);

	private SilicateTestContextParamTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	@SuppressWarnings("SameParameterValue") // Shush.
	private static <T> GlobalParameterKey<T> register(String name, Class<T> clazz) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID + "_test", name);
		return Registry.register(
				SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE,
				id,
				new GlobalParameterKey<>(id, clazz)
		);
	}
}
