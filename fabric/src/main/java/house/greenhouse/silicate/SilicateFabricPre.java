package house.greenhouse.silicate;

import house.greenhouse.silicate.platform.SilicatePlatformHelperFabric;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class SilicateFabricPre implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        Silicate.setHelper(new SilicatePlatformHelperFabric());
    }
}