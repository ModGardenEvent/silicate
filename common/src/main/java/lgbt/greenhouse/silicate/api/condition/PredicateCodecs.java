package lgbt.greenhouse.silicate.api.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal {@link GamePredicate} codecs.
 */
@ApiStatus.Internal
final class PredicateCodecs {
	public static final Codec<Either<GamePredicate<?>, Boolean>> EITHER_BOOLEAN_CODEC = Codec.either(
			GamePredicate.DISPATCH_CODEC,
			Codec.BOOL
	);
	public static final Codec<GamePredicate<?>> OR_BOOLEAN_DISPATCH_CODEC = EITHER_BOOLEAN_CODEC
			.xmap(
					either -> {
						if (either.right().isPresent()) {
							return new AlwaysPredicate(either.right().get());
						} else {
							return either.orThrow();
						}
					},
					predicate -> {
						if (predicate instanceof AlwaysPredicate(boolean value)) {
							return Either.right(value);
						} else {
							return Either.left(predicate);
						}
					}
			);

	private PredicateCodecs() {}
}
