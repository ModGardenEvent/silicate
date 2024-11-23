package house.greenhouse.silicate;

import house.greenhouse.silicate.platform.SilicatePlatformHelper;
import house.greenhouse.silicate.test.SilicateGameTests;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApiStatus.Internal
public class Silicate {
	public static final String MOD_ID = "silicate";
	public static final String MOD_NAME = "Silicate";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
	public static final List<Class<?>> GAME_TESTS = List.of(
		SilicateGameTests.class
	);

	private static SilicatePlatformHelper helper;

	public static void init() {
		LOG.info("Initializing Silicate");
	}
	
	public static SilicatePlatformHelper getHelper() {
		return helper;
	}
	
	public static void setHelper(SilicatePlatformHelper helper) {
		Silicate.helper = helper;
	}
	
	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}