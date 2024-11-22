package house.greenhouse.silicate;


import house.greenhouse.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import static house.greenhouse.silicate.Silicate.MOD_ID;

@ApiStatus.Internal
@Mod(MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
		Silicate.init();
	}
}