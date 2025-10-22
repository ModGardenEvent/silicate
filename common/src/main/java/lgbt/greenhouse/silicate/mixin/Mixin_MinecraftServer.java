package lgbt.greenhouse.silicate.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import lgbt.greenhouse.silicate.Silicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class Mixin_MinecraftServer {
	@Inject(
			method = "<init>",
			at = @At("CTOR_HEAD")
	)
	private void onInit(
			Thread serverThread,
			LevelStorageSource.LevelStorageAccess storageSource,
			PackRepository packRepository,
			WorldStem worldStem,
			Proxy proxy,
			DataFixer fixerUpper,
			Services services,
			ChunkProgressListenerFactory progressListenerFactory,
			CallbackInfo ci
	) {
		Silicate.setServer((MinecraftServer) (Object) this);
	}
}
