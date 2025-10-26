package lgbt.greenhouse.silicate.api.context.param;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of all possible parameters in a context.
 */
public final class ParameterSet {
	private final Set<GlobalParameterKey<?>> required;
	private final Set<GlobalParameterKey<?>> all;

	private ParameterSet(Set<GlobalParameterKey<?>> required, Set<GlobalParameterKey<?>> optional) {
		this.required = required;
		this.all = Sets.union(required, optional);
	}

	/**
	 * Whether the parameter type is present in this set and may be used.
	 */
	public <T> boolean hasParam(GlobalParameterKey<T> type) {
		return all.contains(type);
	}

	/**
	 * If the parameter is mandatory.
	 */
	public <T> boolean isRequired(GlobalParameterKey<T> type) {
		return required.contains(type);
	}

	public Set<GlobalParameterKey<?>> getRequired() {
		return required;
	}

	public Set<GlobalParameterKey<?>> getAll() {
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
		private final Set<GlobalParameterKey<?>> required = new HashSet<>();
		private final Set<GlobalParameterKey<?>> optional = new HashSet<>();

		private Builder() {}

		public static Builder of() {
			return new Builder();
		}

		public <T> Builder required(GlobalParameterKey<T> param) {
			required.add(param);
			return this;
		}

		public <T> Builder optional(GlobalParameterKey<T> param) {
			optional.add(param);
			return this;
		}

		public ParameterSet build() {
			return new ParameterSet(Set.copyOf(required), Set.copyOf(optional));
		}
	}
}
