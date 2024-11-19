package house.greenhouse.examplemod;

import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ExampleMod.LOG.info("Hello Fabric world!");
        ExampleMod.init();
    }
}
