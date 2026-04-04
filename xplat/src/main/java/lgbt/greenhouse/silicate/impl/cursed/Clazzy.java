package lgbt.greenhouse.silicate.impl.cursed;

import org.jetbrains.annotations.ApiStatus;

/**
 * Cursed utilities for dealing with {@link Class}.
 */
@ApiStatus.Internal
public final class Clazzy {
	private Clazzy() {}

	/// # ⚠️ Warning ⚠️
	///
	/// Double check that the [Class] entered is the correct one.
	/// If it is not, it will result in a [ClassCastException] at runtime.
	/// @param clazz the clazz you want to forcefully convert
	/// @return the force-casted clazz
	/// @param <T> the type represented by the target-type clazz
	@SuppressWarnings({"unchecked"}) // the caller accepts responsibility
	public static <T> Class<T> cast(Class<?> clazz) {
		return (Class<T>) clazz;
	}
}
