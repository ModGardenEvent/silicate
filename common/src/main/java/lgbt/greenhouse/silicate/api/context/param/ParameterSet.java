package lgbt.greenhouse.silicate.api.context.param;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of all possible parameters in a context.
 */
public final class ParameterSet {
	private final Set<GlobalParameterType<?>> required;
	private final Set<GlobalParameterType<?>> all;

	private ParameterSet(Set<GlobalParameterType<?>> required, Set<GlobalParameterType<?>> optional) {
		this.required = required;
		this.all = Sets.union(required, optional);
	}

	/**
	 * Whether the parameter type is present in this set and may be used.
	 */
	public <T> boolean hasParam(GlobalParameterType<T> type) {
		return all.contains(type);
	}

	/**
	 * If the parameter is mandatory.
	 */
	public <T> boolean isRequired(GlobalParameterType<T> type) {
		return required.contains(type);
	}

	public Set<GlobalParameterType<?>> getRequired() {
		return required;
	}

	public Set<GlobalParameterType<?>> getAll() {
		return all;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParameterSet set) {
			return getRequired().equals(set.getRequired()) && getAll().equals(set.getAll());
		} else {
			return false;
		}
	}

	public static final class Builder {
		private final Set<GlobalParameterType<?>> required = new HashSet<>();
		private final Set<GlobalParameterType<?>> optional = new HashSet<>();

		private Builder() {}

		public static Builder of() {
			return new Builder();
		}

		public <T> Builder required(GlobalParameterType<T> param) {
			required.add(param);
			return this;
		}

		public <T> Builder optional(GlobalParameterType<T> param) {
			optional.add(param);
			return this;
		}

		public ParameterSet build() {
			return new ParameterSet(Set.copyOf(required), Set.copyOf(optional));
		}
	}
}
