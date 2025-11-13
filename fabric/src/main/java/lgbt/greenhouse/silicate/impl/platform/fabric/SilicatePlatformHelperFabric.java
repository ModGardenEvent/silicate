package lgbt.greenhouse.silicate.impl.platform.fabric;

import lgbt.greenhouse.silicate.impl.platform.Platform;
import lgbt.greenhouse.silicate.impl.platform.Side;
import lgbt.greenhouse.silicate.impl.platform.SilicatePlatformHelper;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class SilicatePlatformHelperFabric implements SilicatePlatformHelper {
	@Override
	public @NotNull Platform getPlatform() {
		return Platform.FABRIC;
	}

	@Override
	public boolean isModLoaded(@NotNull String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public @NotNull ServerPlayer createFakePlayer(@NotNull ServerLevel level) {
		return FakePlayer.get(level, DEFAULT_PROFILE);
	}

	@Override
	public @NotNull Side getSide() {
		return switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> Side.CLIENT;
			case SERVER -> Side.DEDICATED_SERVER;
		};
	}
}
