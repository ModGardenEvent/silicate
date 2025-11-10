package lgbt.greenhouse.silicate.impl;

import net.minecraft.server.MinecraftServer;
import lgbt.greenhouse.silicate.impl.platform.SilicatePlatformHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class Silicate {
	private static @Nullable MinecraftServer server;

	private static @Nullable SilicatePlatformHelper helper;

	public static void init() {
	}

	public static void setServer(MinecraftServer server) {
		Silicate.server = server;
	}

	public static @Nullable MinecraftServer getServer() {
		return server;
	}

	public static @Nullable SilicatePlatformHelper getHelper() {
		return helper;
	}

	public static void setHelper(SilicatePlatformHelper helper) {
		Silicate.helper = helper;
	}
}
