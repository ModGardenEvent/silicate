package lgbt.greenhouse.silicate.api.condition.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import net.minecraft.core.Holder;

import java.util.List;

/**
 * A predicate that succeeds if any of its conditions pass.
 */
public final class AnyPredicate extends CompoundPredicate<AnyPredicate> {
	public AnyPredicate(Deferred<List<Holder<GamePredicate<?>>>> conditions) {
		super(conditions);
	}

	@Override
	public boolean test(GameContext ctx) {
		return this.getConditions().get(ctx).stream()
				.anyMatch(condition -> condition.value().test(ctx));
	}

	@Override
	public GamePredicate.Type<AnyPredicate> getType() {
		return SilicatePredicateTypes.ANY;
	}

	public static final class Type extends CompoundPredicate.Type<AnyPredicate> {
		@Override
		protected MapCodec<AnyPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(AnyPredicate.class))
					.build();
		}
	}
}
