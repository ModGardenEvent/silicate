package net.modgarden.silicate.test.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.modgarden.silicate.api.condition.GameCondition;

public record ExpectedResultCondition(Holder<GameCondition<?>> condition, String name, boolean shouldSucceed) {
	public static final Codec<ExpectedResultCondition> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			GameCondition.CODEC
					.fieldOf("condition")
					.forGetter(ExpectedResultCondition::condition),
			Codec.STRING
					.fieldOf("name")
					.forGetter(ExpectedResultCondition::name),
			Codec.BOOL
					.optionalFieldOf("should_succeed", true)
					.forGetter(ExpectedResultCondition::shouldSucceed)
	).apply(inst, ExpectedResultCondition::new));
}
