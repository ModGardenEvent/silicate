package lgbt.greenhouse.silicate.test;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.test.instance.PredicatesTestInstance;
import lgbt.greenhouse.silicate.test.instance.ParameterMapTestInstance;
import lgbt.greenhouse.silicate.test.instance.GameContextTestInstance;
import org.jetbrains.annotations.ApiStatus;

import static lgbt.greenhouse.silicate.impl.SilicateConstants.MOD_ID;

public class SilicateTestInstanceTypes {
	static {
		register("predicates", PredicatesTestInstance.CODEC);
		register("parameter_map", ParameterMapTestInstance.CODEC);
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
