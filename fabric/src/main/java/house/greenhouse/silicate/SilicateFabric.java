package house.greenhouse.silicate;

import net.fabricmc.api.ModInitializer;

public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Silicate.LOG.info("Hello Fabric world!");
		Silicate.init();
	}
}