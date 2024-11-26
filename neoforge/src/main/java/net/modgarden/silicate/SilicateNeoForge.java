package net.modgarden.silicate;


import net.modgarden.silicate.platform.SilicatePlatformHelperNeoForge;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import static net.modgarden.silicate.Silicate.MOD_ID;

@ApiStatus.Internal
@Mod(MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
		Silicate.GAME_TESTS.forEach(SilicateNeoForge::registerGameTest);
		Silicate.init();
	}

	@SuppressWarnings("deprecation") // We don't care about Neo's warnings.
	private static void registerGameTest(Class<?> clazz) {
		GameTestRegistry.register(clazz);
	}
}
