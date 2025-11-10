package lgbt.greenhouse.silicate.api.condition.meta;

import lgbt.greenhouse.silicate.api.type.ValueType;

public record DynamicValue(
		ValueType<?> type,
		Object value
) {
}
