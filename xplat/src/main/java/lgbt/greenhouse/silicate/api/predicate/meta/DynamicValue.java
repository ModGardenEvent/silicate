package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.ValueType;
import org.jetbrains.annotations.NotNull;

/**
 * A value whose {@link ValueType} is resolved dynamically at decode-time.
 * @param type the value's type
 * @param value the value in question
 */
public record DynamicValue(
		ValueType<?> type,
		Deferred<Object> value
) {
	static MapCodec<DynamicValue> createCodec(String dynamicKey, String key) {
		return new KeyDispatchCodec<>(
				dynamicKey,
				ValueType.CODEC,
				value -> DataResult.success(value.type()),
				valueType1 -> {
					@SuppressWarnings("unchecked")
					var valueType = (ValueType<@NotNull Object>) valueType1;

					//noinspection unchecked // erasing wildcard in ParameterKey<?>
					MapCodec<DynamicValue> parameterKeyCodec = ParameterKey.TEMPLATE_CODEC.fieldOf(key)
							.xmap(
									key1 -> new DynamicValue(
											valueType,
											new Deferred<>(
													(ParameterKey<@NotNull Object>) key1,
													valueType
											)
									),
									dynamicValue -> dynamicValue.value.parameterKey()
							);
					MapCodec<DynamicValue> codec;
					Codec<Object> valueTypeCodec = valueType.codec();
					if (valueTypeCodec != null) { // especially in the case of template-only types
						codec = valueTypeCodec.fieldOf(key)
								.xmap(
										v -> new DynamicValue(valueType, new Deferred<>(v)),
										DynamicValue::type
								);
					} else {
						codec = parameterKeyCodec;
					}

					codec = Codec.mapEither(
							codec,
							parameterKeyCodec
					)
							.xmap(
									either -> {
										if (either.right().isPresent()) {
											return either.right().get();
										} else {
											return either.orThrow();
										}
									},
									Either::left
							);
					return DataResult.success(codec);
				}
		);
	}
}
