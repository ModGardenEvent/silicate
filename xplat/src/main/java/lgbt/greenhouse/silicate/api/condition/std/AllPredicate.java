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
 * A predicate that succeeds only if all given conditions succeed.
 */
public final class AllPredicate extends CompoundPredicate<AllPredicate> {
	public AllPredicate(Deferred<List<Holder<GamePredicate<?>>>> conditions) {
		super(conditions);
	}

	@Override
	public boolean test(GameContext ctx) {
		return this.getConditions().get(ctx).stream()
				.allMatch(condition -> condition.value().test(ctx));
	}

	@Override
	public GamePredicate.Type<AllPredicate> getType() {
		return SilicatePredicateTypes.ALL;
	}

	public static final class Type extends CompoundPredicate.Type<AllPredicate> {
		@Override
		public MapCodec<AllPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(AllPredicate.class))
					.build();
		}
	}
}
