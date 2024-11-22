package house.greenhouse.silicate.platform;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@ApiStatus.Internal
public interface SilicatePlatformHelper {
	/**
	 * Default UUID, for fake players not associated with a specific (human) player.
	 * <br>
	 * From Fabric API.
	 */
	UUID DEFAULT_UUID = UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77");
	/**
	 * From Fabric API.
	 */
	GameProfile DEFAULT_PROFILE = new GameProfile(DEFAULT_UUID, "[Minecraft]");
	
	/**
	 * Gets the current platform
	 *
	 * @return An enum value representing the current platform.
	 */
	Platform getPlatform();

	/**
	 * Checks if a mod with the given id is loaded.
	 *
	 * @param modId The mod to check if it is loaded.
	 * @return True if the mod is loaded, false otherwise.
	 */
	boolean isModLoaded(String modId);

	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	boolean isDevelopmentEnvironment();
	
	/**
	 * Creates a fake player entity. This is a platform-dependent operation.
	 * @return The fake player.
	 */
	ServerPlayer createFakePlayer(ServerLevel level);
}