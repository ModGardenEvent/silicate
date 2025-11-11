package lgbt.greenhouse.silicate.api.predicate.meta;

import lgbt.greenhouse.silicate.api.type.ValueType;

public record DynamicValue(
		ValueType<?> type,
		Object value
) {
}
