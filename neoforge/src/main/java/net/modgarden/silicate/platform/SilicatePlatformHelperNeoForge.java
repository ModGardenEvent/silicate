package net.modgarden.silicate.platform;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicatePlatformHelperNeoForge implements SilicatePlatformHelper {
	@Override
	public Platform getPlatform() {
		return Platform.NEOFORGE;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public ServerPlayer createFakePlayer(ServerLevel level) {
		return new FakePlayer(level, DEFAULT_PROFILE);
	}
}
