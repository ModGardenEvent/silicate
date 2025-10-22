package lgbt.greenhouse.silicate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.SharedConstants;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.condition.GameCondition;
import lgbt.greenhouse.silicate.platform.SilicatePlatformHelperFabric;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Silicate.setHelper(new SilicatePlatformHelperFabric());
		// Enabled for debug purposes.
		SharedConstants.IS_RUNNING_IN_IDE = Silicate.getHelper().isDevelopmentEnvironment();
		Silicate.init();
		SilicateBuiltInRegistries.registerAll();

		DynamicRegistries.registerSynced(SilicateRegistries.CONDITION_TEMPLATE, GameCondition.TYPED_CODEC);
	}
}
