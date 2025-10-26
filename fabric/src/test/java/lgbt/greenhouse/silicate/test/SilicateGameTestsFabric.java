package lgbt.greenhouse.silicate.test;

import net.fabricmc.api.ModInitializer;

public class SilicateGameTestsFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		SilicateTestInstanceTypes.registerAll();
		SilicateTestGlobalParameterKeys.registerAll();
	}
}
