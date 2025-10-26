package lgbt.greenhouse.silicate.test;

import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.MOD_ID;

public final class SilicateTestValueTypes {
	public static final ValueType<Projectile> PROJECTILE = register("projectile_entity", Projectile.class);

	private SilicateTestValueTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	@SuppressWarnings("SameParameterValue") // Shush.
	private static <T> ValueType<T> register(String name, Class<T> clazz) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID + "_test", name);
		return Registry.register(
				SilicateBuiltInRegistries.VALUE_TYPE,
				id,
				new ValueType<>(clazz)
		);
	}
}
