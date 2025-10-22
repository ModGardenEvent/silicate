package lgbt.greenhouse.silicate.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.duck.Duck_AbstractClientPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractClientPlayer.class)
public abstract class Mixin_AbstractClientPlayer extends Player implements Duck_AbstractClientPlayer {
	@Shadow
	@Nullable
	protected abstract PlayerInfo getPlayerInfo();

	private Mixin_AbstractClientPlayer(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Override
	public @Nullable PlayerInfo silicate$getPlayerInfo() {
		return this.getPlayerInfo();
	}
}
