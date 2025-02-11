package net.modgarden.silicate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.SharedConstants;
import net.modgarden.silicate.api.SilicateRegistries;
import net.modgarden.silicate.api.condition.GameCondition;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		// Enabled for debug purposes.
		SharedConstants.IS_RUNNING_IN_IDE = Silicate.getHelper().isDevelopmentEnvironment();
		Silicate.init();

		DynamicRegistries.registerSynced(SilicateRegistries.CONDITION_TEMPLATE, GameCondition.TYPED_CODEC);
	}
}
