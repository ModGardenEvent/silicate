package lgbt.greenhouse.silicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import lgbt.greenhouse.silicate.platform.SilicatePlatformHelper;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;

@ApiStatus.Internal
public class Silicate {
	public static final String MOD_ID = "silicate";
	public static final String MOD_NAME = "Silicate";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
	private static MinecraftServer server;

	private static SilicatePlatformHelper helper;

	public static void init() {
		LOG.info("Initializing Silicate");
	}

	public static void setServer(MinecraftServer server) {
		Silicate.server = server;
	}

	public static MinecraftServer getServer() {
		return server;
	}

	public static SilicatePlatformHelper getHelper() {
		return helper;
	}

	public static void setHelper(SilicatePlatformHelper helper) {
		Silicate.helper = helper;
	}

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
