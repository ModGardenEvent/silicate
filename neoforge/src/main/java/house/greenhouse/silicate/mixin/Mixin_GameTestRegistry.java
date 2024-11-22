package house.greenhouse.silicate.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.silicate.test.SilicateGameTests;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.lang.reflect.Method;

@Mixin(GameTestRegistry.class)
public class Mixin_GameTestRegistry {
	@ModifyVariable(
			method = "turnMethodIntoTestFunction",
			at = @At(
					value = "STORE",
					target = "Lnet/neoforged/neoforge/gametest/GameTestHooks;getTemplateNamespace(Ljava/lang/reflect/Method;)Ljava/lang/String;"
			),
			index = 6
	)
	private static String disableNeoForgeBreakage(
			String value,
			@Local GameTest gameTest,
			@Local(argsOnly = true) Method testMethod
	) {
		return testMethod.getDeclaringClass().equals(SilicateGameTests.class) ? gameTest.template() : value;
	}
}