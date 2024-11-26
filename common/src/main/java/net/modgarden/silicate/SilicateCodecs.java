package net.modgarden.silicate;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Extra codecs for Silicate.
 */
@ApiStatus.Internal
public final class SilicateCodecs {
	public static final Codec<HolderSet<EntityType<?>>> ENTITY_TYPE_HOLDER_SET = RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE);

	private SilicateCodecs() {}
}
