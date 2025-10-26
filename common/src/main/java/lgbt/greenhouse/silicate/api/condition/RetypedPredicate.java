package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;

/**
 * Implements {@link TypedGamePredicate} on a regular {@link GamePredicate}.
 * This is meant for use by datapacks.
 */
public record RetypedPredicate(
		GlobalParameterType<?> paramType,
		Holder<GamePredicate<?>> condition
) implements TypedGamePredicate<RetypedPredicate, Object> {
	public static final MapCodec<RetypedPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GlobalParameterType.ANY_CODEC
					.fieldOf("param_type")
					.forGetter(RetypedPredicate::paramType),
			GamePredicate.CODEC
					.fieldOf("condition")
					.forGetter(RetypedPredicate::condition)
	).apply(instance, RetypedPredicate::new));

	@SuppressWarnings("unchecked") // Checked at runtime.
	@Override
	public GlobalParameterType<Object> getParamType() {
		return (GlobalParameterType<Object>) paramType;
	}

	@Override
	public boolean test(GameContext context) {
		return condition.value().test(context);
	}

	@Override
	public MapCodec<RetypedPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<RetypedPredicate> getType() {
		return PredicateTypes.RETYPED;
	}
}
