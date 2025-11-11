package lgbt.greenhouse.silicate.impl.platform.fabric;

import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.impl.Silicate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.SharedConstants;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Silicate.setHelper(new SilicatePlatformHelperFabric());
		// Enabled for debug purposes.
		assert Silicate.getHelper() != null;
		SharedConstants.IS_RUNNING_IN_IDE = Silicate.getHelper().isDevelopmentEnvironment();
		Silicate.init();
		SilicateBuiltInRegistries.registerAll();

		DynamicRegistries.registerSynced(SilicateRegistries.CONDITION, GamePredicate.DISPATCH_CODEC);
	}
}
