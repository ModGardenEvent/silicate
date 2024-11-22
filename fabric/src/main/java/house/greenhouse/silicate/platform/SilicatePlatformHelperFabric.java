package house.greenhouse.silicate.platform;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicatePlatformHelperFabric implements SilicatePlatformHelper {
	@Override
	public Platform getPlatform() {
		return Platform.FABRIC;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
	
	@Override
	public ServerPlayer createFakePlayer(ServerLevel level) {
		return FakePlayer.get(level, DEFAULT_PROFILE);
	}
}