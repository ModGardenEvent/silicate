package lgbt.greenhouse.silicate.test;

import lgbt.greenhouse.silicate.test.SilicateTestContextParamTypes;
import lgbt.greenhouse.silicate.test.SilicateTestInstanceTypes;
import net.fabricmc.api.ModInitializer;

public class SilicateGameTestsFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		SilicateTestInstanceTypes.registerAll();
		SilicateTestContextParamTypes.registerAll();
	}
}
