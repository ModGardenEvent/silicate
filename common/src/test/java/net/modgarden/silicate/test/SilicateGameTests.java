package net.modgarden.silicate.test;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SilicateGameTests {
	public static void registerAll() {
		SilicateTestInstanceTypes.registerAll();
		SilicateTestContextParamTypes.registerAll();
	}
}
