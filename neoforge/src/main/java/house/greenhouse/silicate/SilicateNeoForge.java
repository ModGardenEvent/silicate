package house.greenhouse.silicate;


import house.greenhouse.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod(Silicate.MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.init();
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
	}
}