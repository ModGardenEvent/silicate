package lgbt.greenhouse.silicate.impl.cursed;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Everything they told you not to do, all in one class.
 */
public final class UnsafeUtil {
	private static final Unsafe THE_UNSAFE;

	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			THE_UNSAFE = (Unsafe) theUnsafe.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private UnsafeUtil() {}

	/**
	 * @see Unsafe#allocateInstance(Class)
	 */
	public static <T> T allocateInstance(Class<T> clazz) {
		try {
			//noinspection unchecked
			return (T) THE_UNSAFE.allocateInstance(clazz);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
}
