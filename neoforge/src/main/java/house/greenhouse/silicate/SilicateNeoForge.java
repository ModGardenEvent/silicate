package house.greenhouse.silicate;


import house.greenhouse.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import org.jetbrains.annotations.ApiStatus;

import static house.greenhouse.silicate.Silicate.MOD_ID;

@ApiStatus.Internal
@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.MOD
)
@Mod(MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
		Silicate.init();
	}
	
	@SubscribeEvent
	public static void registerTests(RegisterGameTestsEvent event) {
		Silicate.GAME_TESTS.forEach(event::register);
	}
}