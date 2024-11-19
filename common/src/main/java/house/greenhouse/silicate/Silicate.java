package house.greenhouse.silicate;

import house.greenhouse.silicate.platform.ExamplePlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Silicate {
    public static final String MOD_ID = "examplemod";
    public static final String MOD_NAME = "Example Mod";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    private static ExamplePlatformHelper helper;

    public static void init() {

    }

    public static ExamplePlatformHelper getHelper() {
        return helper;
    }

    public static void setHelper(ExamplePlatformHelper helper) {
        Silicate.helper = helper;
    }
}