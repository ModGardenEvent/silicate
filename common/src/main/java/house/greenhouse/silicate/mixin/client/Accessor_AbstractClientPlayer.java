package house.greenhouse.silicate.mixin.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractClientPlayer.class)
public interface Accessor_AbstractClientPlayer {
	@Invoker("getPlayerInfo")
	@Nullable
	PlayerInfo getPlayerInfo();
}