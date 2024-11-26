package net.modgarden.silicate;

import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		// Enabled for debug purposes.
		SharedConstants.IS_RUNNING_IN_IDE = Silicate.getHelper().isDevelopmentEnvironment();
		Silicate.init();
	}
}
