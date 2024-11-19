package house.greenhouse.examplemod;


import house.greenhouse.examplemod.platform.ExamplePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ExampleMod.MOD_ID)
public class ExampleModNeoForge {

    public ExampleModNeoForge(IEventBus eventBus) {
        ExampleMod.init();
        ExampleMod.setHelper(new ExamplePlatformHelperNeoForge());
    }
}