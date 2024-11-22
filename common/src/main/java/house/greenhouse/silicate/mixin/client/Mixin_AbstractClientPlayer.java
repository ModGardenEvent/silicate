package house.greenhouse.silicate.mixin.client;

import com.mojang.authlib.GameProfile;
import house.greenhouse.silicate.duck.Duck_AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractClientPlayer.class)
public abstract class Mixin_AbstractClientPlayer extends Player implements Duck_AbstractClientPlayer {
	@Shadow
	@javax.annotation.Nullable
	protected abstract PlayerInfo getPlayerInfo();
	
	private Mixin_AbstractClientPlayer(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
		super(level, pos, yRot, gameProfile);
	}
	
	@Override
	public @Nullable PlayerInfo silicate$getPlayerInfo() {
		return this.getPlayerInfo();
	}
}