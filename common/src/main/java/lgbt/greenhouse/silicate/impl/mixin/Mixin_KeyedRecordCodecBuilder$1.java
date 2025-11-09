package lgbt.greenhouse.silicate.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import dev.lukebemish.codecextras.record.KeyedRecordCodecBuilder;
import lgbt.greenhouse.silicate.api.type.ValueType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
@Mixin(
		targets = "dev/lukebemish/codecextras/record/KeyedRecordCodecBuilder$1"
)
public final class Mixin_KeyedRecordCodecBuilder$1 {
	@Unique
	private static final MethodHandle NEW_FIELD_HANDLE;
	@Unique
	private static final MethodHandle GETTER_HANDLE;
	@Unique
	private static final MethodHandle KEY_HANDLE;
	@Unique
	private static final MethodHandle COUNT_GETTER_HANDLE;

	static {
		// dirty reflection hack. don't try this at home.
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			Class<?> fieldClass = Class.forName("dev.lukebemish.codecextras.record.KeyedRecordCodecBuilder.Field");

			Constructor<?> newField = fieldClass.getDeclaredConstructor(KeyedRecordCodecBuilder.Key.class, Function.class, MapCodec.class);
			newField.setAccessible(true);
			NEW_FIELD_HANDLE = lookup.unreflectConstructor(newField);

			Method getterComponent = fieldClass.getMethod("getter");
			getterComponent.setAccessible(true);
			GETTER_HANDLE = lookup.unreflect(getterComponent);

			Method keyComponent = fieldClass.getMethod("key");
			keyComponent.setAccessible(true);
			KEY_HANDLE = lookup.unreflect(keyComponent);

			Field countField = KeyedRecordCodecBuilder.Key.class.getDeclaredField("count");
			countField.setAccessible(true);
			COUNT_GETTER_HANDLE = lookup.unreflectGetter(countField);
		} catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@WrapOperation(
			method = "decode",
			at = @At(value = "INVOKE", target = "Ldev/lukebemish/codecextras/record/KeyedRecordCodecBuilder$1;decodePartial(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/MapLike;Ldev/lukebemish/codecextras/record/KeyedRecordCodecBuilder$Container;Ldev/lukebemish/codecextras/record/KeyedRecordCodecBuilder$Field;Ljava/util/List;)V")
	)
	private void silicate$dispatchAtHome(
			@Coerce MapCodec<Object> instance,
			DynamicOps<Object> ops,
			MapLike<Object> input,
			KeyedRecordCodecBuilder.Container container,
			@Coerce Object field,
			List<DataResult.Error<?>> errors,
			Operation<Void> original,
			@Local(name = "keys") KeyedRecordCodecBuilder.Key<?>[] keys
	) throws Throwable {
		KeyedRecordCodecBuilder.Key<?> key = (KeyedRecordCodecBuilder.Key<?>) KEY_HANDLE.invokeExact(field);
		@SuppressWarnings("unchecked") Function<Object, Object> getter = (Function<Object, Object>) GETTER_HANDLE.invokeExact(field);
		int count = (int) COUNT_GETTER_HANDLE.invokeExact(key);
		Object previous = container.get(keys[count - 1]);
		Object value = container.get(keys[count]);
		if (value instanceof Unit && previous instanceof ValueType<?> valueType) {
			Object newField = NEW_FIELD_HANDLE.invokeExact(key, getter, MapCodec.assumeMapUnsafe(valueType.codec()));
			original.call(instance, ops, input, container, newField, errors);
		}
	}
}
