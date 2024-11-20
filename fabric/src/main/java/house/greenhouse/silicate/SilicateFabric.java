package house.greenhouse.silicate;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Silicate.LOG.info("Hello Fabric world!");
		Silicate.init();
	}
}