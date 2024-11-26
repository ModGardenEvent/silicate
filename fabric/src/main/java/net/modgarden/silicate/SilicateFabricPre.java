package net.modgarden.silicate;

import net.modgarden.silicate.platform.SilicatePlatformHelperFabric;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabricPre implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		Silicate.setHelper(new SilicatePlatformHelperFabric());
	}
}
