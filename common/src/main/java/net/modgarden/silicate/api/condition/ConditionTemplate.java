package net.modgarden.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.modgarden.silicate.api.SilicateBuiltInRegistries;
import net.modgarden.silicate.api.SilicateRegistries;
import net.modgarden.silicate.api.context.GameContext;

/**
 * A condition that is registered in a datapack. The condition is requested lazily to prevent a Codec Catastrophe.
 */
@SuppressWarnings("rawtypes") // This class does not care about the type and does not require it.
public class ConditionTemplate implements GameCondition {
	private GameCondition<?> inner;
	private final ResourceLocation id;

	public ConditionTemplate(ResourceLocation id) {
		this.id = id;
	}

	private GameCondition<?> getInner() {
		if (inner == null) {
			inner = SilicateBuiltInRegistries.lookupOrThrow(SilicateRegistries.CONDITION_TEMPLATE).getOrThrow(ResourceKey.create(SilicateRegistries.CONDITION_TEMPLATE, id)).value();
		}

		return inner;
	}

	@Override
	public boolean test(Object o) {
		return test((GameContext) o);
	}

	@Override
	public boolean test(GameContext context) {
		return getInner().test(context);
	}

	@Override
	public MapCodec<?> getCodec() {
		return getInner().getCodec();
	}

	@Override
	public GameConditionType<?> getType() {
		return getInner().getType();
	}
}
