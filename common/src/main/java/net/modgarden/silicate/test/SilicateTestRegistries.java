package net.modgarden.silicate.test;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.modgarden.silicate.Silicate;
import net.modgarden.silicate.api.condition.GameCondition;

public final class SilicateTestRegistries {
	public static final ResourceKey<Registry<GameCondition<?>>> CONDITION = create("condition");

	private static <T> ResourceKey<Registry<T>> create(String name) {
		return ResourceKey.createRegistryKey(Silicate.id(name));
	}
}
