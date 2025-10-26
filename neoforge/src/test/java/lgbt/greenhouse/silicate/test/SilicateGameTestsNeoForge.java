package lgbt.greenhouse.silicate.test;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import static lgbt.greenhouse.silicate.Silicate.MOD_ID;

@Mod(MOD_ID + "_test")
public class SilicateGameTestsNeoForge {
	@EventBusSubscriber(
			modid = MOD_ID + "_test"
	)
	public static class Events {
		@SubscribeEvent
		public static void registerContents(RegisterEvent event) {
			register(event, Registries.TEST_INSTANCE_TYPE, SilicateTestInstanceTypes::registerAll);
			register(event, SilicateRegistries.CONTEXT_PARAM_TYPE, SilicateTestGlobalParameterKeys::registerAll);
		}

		private static void register(RegisterEvent event, ResourceKey<? extends Registry<?>> requiredKey,
									 Runnable runnable) {
			if (requiredKey != event.getRegistryKey())
				return;
			runnable.run();
		}
	}
}
