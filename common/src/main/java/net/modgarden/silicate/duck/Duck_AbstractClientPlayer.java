package net.modgarden.silicate.duck;

import net.minecraft.client.multiplayer.PlayerInfo;
import org.jetbrains.annotations.Nullable;

/// A duck interface created particularly to prevent dedicated server crashes.
///
/// This interface no-ops when using `instanceof` on the dedicated server and fails when
/// casting.
public interface Duck_AbstractClientPlayer {
	@Nullable
	PlayerInfo silicate$getPlayerInfo();
}
