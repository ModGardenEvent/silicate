package house.greenhouse.silicate;


import house.greenhouse.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Silicate.MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.init();
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
	}
}