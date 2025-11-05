package lgbt.greenhouse.silicate.impl;

import net.minecraft.server.MinecraftServer;
import lgbt.greenhouse.silicate.impl.platform.SilicatePlatformHelper;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class Silicate {
	private static MinecraftServer server;

	private static SilicatePlatformHelper helper;

	public static void init() {
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
}
