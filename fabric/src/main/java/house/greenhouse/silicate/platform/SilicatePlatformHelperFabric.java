package house.greenhouse.silicate.platform;

import net.fabricmc.loader.api.FabricLoader;
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
}