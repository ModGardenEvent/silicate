package net.modgarden.silicate.test;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.modgarden.silicate.api.SilicateBuiltInRegistries;
import net.modgarden.silicate.api.context.param.ContextParamType;
import org.jetbrains.annotations.ApiStatus;

import static net.modgarden.silicate.Silicate.MOD_ID;

public class SilicateTestContextParamTypes {
	public static final ContextParamType<Projectile> PROJECTILE = register("projectile", Projectile.class);

	private SilicateTestContextParamTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	@SuppressWarnings("SameParameterValue") // Shush.
	private static <T> ContextParamType<T> register(String name, Class<T> clazz) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID + "_test", name);
		return Registry.register(
				SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE,
				id,
				new ContextParamType<>(id, clazz)
		);
	}
}
