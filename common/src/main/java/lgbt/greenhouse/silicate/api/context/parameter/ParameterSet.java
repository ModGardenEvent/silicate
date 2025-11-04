package lgbt.greenhouse.silicate.api.context.parameter;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of all possible global parameters in a context.
 */
public final class ParameterSet {
	private final Set<GlobalParameterKey<?>> required;
	private final Set<GlobalParameterKey<?>> all;

	private ParameterSet(Set<GlobalParameterKey<?>> required, Set<GlobalParameterKey<?>> optional) {
		this.required = required;
		this.all = Sets.union(required, optional);
	}

	/**
	 * Whether the parameter key is present in this set and may be used.
	 */
	public <T> boolean hasParam(GlobalParameterKey<T> key) {
		return all.contains(key);
	}

	/**
	 * If the parameter is mandatory.
	 */
	public <T> boolean isRequired(GlobalParameterKey<T> key) {
		return required.contains(key);
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

		public <T> Builder required(GlobalParameterKey<T> key) {
			required.add(key);
			return this;
		}

		public <T> Builder optional(GlobalParameterKey<T> key) {
			optional.add(key);
			return this;
		}

		public ParameterSet build() {
			return new ParameterSet(Set.copyOf(required), Set.copyOf(optional));
		}
	}
}
