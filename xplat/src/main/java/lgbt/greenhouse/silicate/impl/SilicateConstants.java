package lgbt.greenhouse.silicate.impl;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public final class SilicateConstants {
	public static final String MOD_ID = "silicate";
	@SuppressWarnings("unused")
	public static final String MOD_NAME = "Silicate";

	private SilicateConstants() {}

	public static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}

	public static Identifier parseId(String value) {
		if (!value.contains(":")) {
			return id(value);
		} else {
			return Identifier.parse(value);
		}
	}

	public static Logger getLogger(String name) {
		return LoggerFactory.getLogger(MOD_NAME + " / " + name);
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
}
