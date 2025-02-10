package net.modgarden.silicate;


import net.minecraft.gametest.framework.GameTestRegistry;
import net.modgarden.silicate.api.SilicateRegistries;
import net.modgarden.silicate.api.condition.GameCondition;
import net.modgarden.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
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

	@SubscribeEvent
	public static void newDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(SilicateRegistries.CONDITION, GameCondition.CODEC);
	}
}
