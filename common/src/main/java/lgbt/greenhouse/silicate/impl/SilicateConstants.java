package lgbt.greenhouse.silicate.impl;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class SilicateConstants {
	public static final String MOD_ID = "silicate";
	@SuppressWarnings("unused")
	public static final String MOD_NAME = "Silicate";

	private SilicateConstants() {}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public static ResourceLocation parseId(String value) {
		if (!value.contains(":")) {
			return id(value);
		} else {
			return ResourceLocation.parse(value);
		}
	}
}
