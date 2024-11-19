package house.greenhouse.silicate;

import house.greenhouse.silicate.platform.SilicatePlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Silicate {
	public static final String MOD_ID = "silicate";
	public static final String MOD_NAME = "Silicate";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	private static SilicatePlatformHelper helper;

	public static void init() {

	}

	public static SilicatePlatformHelper getHelper() {
		return helper;
	}

	public static void setHelper(SilicatePlatformHelper helper) {
		Silicate.helper = helper;
	}
}