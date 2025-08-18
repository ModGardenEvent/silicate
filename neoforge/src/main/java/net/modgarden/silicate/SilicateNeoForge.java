package net.modgarden.silicate;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.modgarden.silicate.api.SilicateBuiltInRegistries;
import net.modgarden.silicate.api.SilicateRegistries;
import net.modgarden.silicate.api.condition.GameCondition;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.context.param.ContextParamTypes;
import net.modgarden.silicate.platform.SilicatePlatformHelperNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import static net.modgarden.silicate.Silicate.MOD_ID;

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
			register(event, SilicateRegistries.CONTEXT_PARAM_TYPE, ContextParamTypes::registerAll);
			register(event, SilicateRegistries.GAME_CONDITION_TYPE, GameConditionTypes::registerAll);
		}

		private static void register(RegisterEvent event, ResourceKey<? extends Registry<?>> requiredKey,
									 Runnable runnable) {
			if (requiredKey != event.getRegistryKey())
				return;
			runnable.run();
		}

		@SubscribeEvent
		public static void newRegistry(NewRegistryEvent event) {
			event.register(SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE);
			event.register(SilicateBuiltInRegistries.GAME_CONDITION_TYPE);
		}

		@SubscribeEvent
		public static void newDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
			event.dataPackRegistry(SilicateRegistries.CONDITION_TEMPLATE, GameCondition.TYPED_CODEC, GameCondition.TYPED_CODEC);
		}
	}
}
