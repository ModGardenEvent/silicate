package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A predicate that succeeds only if all given conditions succeed.
 */
public final class AllPredicate extends CompoundPredicate<AllPredicate> {
	public AllPredicate(List<Holder<GamePredicate<?>>> conditions) {
		super(conditions);
	}

	@Override
	public boolean test(GameContext context) {
		return false;
	}

	@Override
	public GamePredicate.@NotNull Type<AllPredicate> getType() {
		return SilicatePredicateTypes.ALL;
	}

	public static final class Type extends CompoundPredicate.Type<AllPredicate> {
		@Override
		public MapCodec<AllPredicate> getCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(AllPredicate.class))
					.build(PredicateCodecBuilder.findConstructor(
							AllPredicate.class,
							List.class
					));
		}
	}
}
