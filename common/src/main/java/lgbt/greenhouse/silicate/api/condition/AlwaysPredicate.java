package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import org.jetbrains.annotations.NotNull;

/**
 * A predicate that always returns a specific value when tested.
 * @param value The value to return in a test.
 */
public record AlwaysPredicate(boolean value) implements GamePredicate<AlwaysPredicate> {
	public static final MapCodec<AlwaysPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL
					.fieldOf("value")
					.forGetter(AlwaysPredicate::value)
	).apply(instance, AlwaysPredicate::new));

	@Override
	public boolean test(GameContext context) {
		return value;
	}

	@Override
	public @NotNull MapCodec<AlwaysPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Type<AlwaysPredicate> getType() {
		return SilicatePredicateTypes.ALWAYS;
	}
}
