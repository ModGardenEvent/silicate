package net.modgarden.silicate.test;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.resources.ResourceLocation;
import net.modgarden.silicate.test.instance.ConditionsTestInstance;
import net.modgarden.silicate.test.instance.ContextParamMapTestInstance;
import net.modgarden.silicate.test.instance.ContextParamTestinstance;
import net.modgarden.silicate.test.instance.GameContextTestInstance;
import org.jetbrains.annotations.ApiStatus;

import static net.modgarden.silicate.Silicate.MOD_ID;

public class SilicateTestInstanceTypes {
	static {
		register("conditions", ConditionsTestInstance.CODEC);
		register("context_param", ContextParamTestinstance.CODEC);
		register("context_param_map", ContextParamMapTestInstance.CODEC);
		register("game_context", GameContextTestInstance.CODEC);
	}

	private SilicateTestInstanceTypes() {}

	@ApiStatus.Internal
	public static void registerAll() {}

	@SuppressWarnings("SameParameterValue") // Shush.
	private static void register(String name, MapCodec<? extends GameTestInstance> codec) {
		Registry.register(
				BuiltInRegistries.TEST_INSTANCE_TYPE,
				ResourceLocation.fromNamespaceAndPath(MOD_ID + "_test", name),
				codec
		);
	}
}
