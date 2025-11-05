package lgbt.greenhouse.silicate;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;
import lgbt.greenhouse.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.Silicate.MOD_ID;

@ApiStatus.Internal
@Mod(MOD_ID)
public class SilicateNeoForge {
	public SilicateNeoForge(IEventBus eventBus) {
		Silicate.setHelper(new SilicatePlatformHelperNeoForge());
	}

	@EventBusSubscriber(
			modid = MOD_ID
	)
	public static class Events {

		@SubscribeEvent
		public static void registerContents(RegisterEvent event) {
			register(event, SilicateRegistries.GLOBAL_PARAMETER_KEY, GlobalParameterKeys::registerAll);
			register(event, SilicateRegistries.PREDICATE, SilicatePredicateTypes::registerAll);
		}

		private static void register(RegisterEvent event, ResourceKey<? extends Registry<?>> requiredKey,
									 Runnable runnable) {
			if (requiredKey != event.getRegistryKey())
				return;
			runnable.run();
		}

		@SubscribeEvent
		public static void newRegistry(NewRegistryEvent event) {
			event.register(SilicateBuiltInRegistries.GLOBAL_PARAMETER_KEY);
			event.register(SilicateBuiltInRegistries.PREDICATE);
		}

		@SubscribeEvent
		public static void newDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
			event.dataPackRegistry(SilicateRegistries.CONDITION_TEMPLATE, GamePredicate.DISPATCH_CODEC, GamePredicate.DISPATCH_CODEC);
		}
	}
}
