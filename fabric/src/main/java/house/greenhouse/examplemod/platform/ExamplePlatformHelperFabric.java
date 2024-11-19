package house.greenhouse.examplemod.platform;

import net.fabricmc.loader.api.FabricLoader;

public class ExamplePlatformHelperFabric implements ExamplePlatformHelper {

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
