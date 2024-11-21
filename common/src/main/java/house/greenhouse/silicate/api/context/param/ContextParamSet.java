package house.greenhouse.silicate.api.context.param;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of all possible parameters in a context.
 */
public final class ContextParamSet {
	private final Set<ContextParamType<?>> required;
	private final Set<ContextParamType<?>> all;
	
	private ContextParamSet(Set<ContextParamType<?>> required, Set<ContextParamType<?>> optional) {
		this.required = required;
		this.all = Sets.union(required, optional);
	}
	
	/**
	 * Whether the parameter type is present in this set and may be used.
	 */
	public <T> boolean hasParam(ContextParamType<T> type) {
		return all.contains(type);
	}
	
	/**
	 * If the parameter is mandatory.
	 */
	public <T> boolean isRequired(ContextParamType<T> type) {
		return required.contains(type);
	}
	
	public Set<ContextParamType<?>> getRequired() {
		return required;
	}
	
	public Set<ContextParamType<?>> getAll() {
		return all;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContextParamSet set) {
			return getRequired().equals(set.getRequired()) && getAll().equals(set.getAll());
		} else {
			return false;
		}
	}

	public static final class Builder {
		private final Set<ContextParamType<?>> required = new HashSet<>();
		private final Set<ContextParamType<?>> optional = new HashSet<>();
		
		private Builder() {}
		
		public static Builder of() {
			return new Builder();
		}

		public <T> Builder required(ContextParamType<T> param) {
			required.add(param);
			return this;
		}

		public <T> Builder optional(ContextParamType<T> param) {
			optional.add(param);
			return this;
		}
		
		public ContextParamSet build() {
			return new ContextParamSet(Set.copyOf(required), Set.copyOf(optional));
		}
	}
}