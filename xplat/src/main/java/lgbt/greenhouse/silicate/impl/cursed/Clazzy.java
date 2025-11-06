package lgbt.greenhouse.silicate.impl.cursed;

import org.jetbrains.annotations.ApiStatus;

/**
 * Cursed utilities for dealing with {@link Class}.
 */
@ApiStatus.Internal
public final class Clazzy {
	private Clazzy() {}

	/**
	 * <h1 style="color:red;">⚠️ Warning ⚠️</h1>
	 * Double check that the {@link Class} entered is the correct one.
	 * If it is not, it will result in a {@link ClassCastException} at runtime.
	 * @param clazz the clazz you want to forcefully convert
	 * @return the force-casted clazz
	 * @param <T> the type represented by the target-type clazz
	 * @deprecated This isn't deprecated, but if you use this, Java will probably explode. Beware.
	 */
	@SuppressWarnings({"unchecked", "DeprecatedIsStillUsed"}) // the caller accepts responsibility
	@Deprecated
	public static <T> Class<T> cast(Class<?> clazz) {
		return (Class<T>) clazz;
	}
}
