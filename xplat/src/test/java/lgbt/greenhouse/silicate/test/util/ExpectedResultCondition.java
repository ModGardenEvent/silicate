package lgbt.greenhouse.silicate.test.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;

public record ExpectedResultCondition(Holder<GamePredicate<?>> condition, String name, boolean shouldSucceed, boolean shouldThrow) {
	public static final Codec<ExpectedResultCondition> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			GamePredicate.CODEC
					.fieldOf("condition")
					.forGetter(ExpectedResultCondition::condition),
			Codec.STRING
					.fieldOf("name")
					.forGetter(ExpectedResultCondition::name),
			Codec.BOOL
					.optionalFieldOf("should_succeed", true)
					.forGetter(ExpectedResultCondition::shouldSucceed),
			Codec.BOOL
					.optionalFieldOf("should_throw", false)
					.forGetter(ExpectedResultCondition::shouldThrow)
	).apply(inst, ExpectedResultCondition::new));
}
