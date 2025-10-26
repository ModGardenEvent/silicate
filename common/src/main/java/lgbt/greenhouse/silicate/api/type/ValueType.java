package lgbt.greenhouse.silicate.api.type;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Optional;

/**
 * A type wrapping a {@link Class}.
 * @param clazz underlying class
 * @param <T> underlying type
 */
public record ValueType<T>(Class<T> clazz) implements
		GenericDeclaration,
		Type,
		AnnotatedElement,
		TypeDescriptor.OfField<Class<?>>,
		Constable {
	/**
	 * @see Class#isAssignableFrom(Class)
	 */
	public boolean isAssignableFrom(ValueType<?> type) {
		return this.clazz.isAssignableFrom(type.clazz);
	}

	@Override
	public String getTypeName() {
		return this.clazz.getTypeName();
	}

	@Override
	public Optional<? extends ConstantDesc> describeConstable() {
		return this.clazz.describeConstable();
	}

	@Override
	public boolean isArray() {
		return this.clazz.isArray();
	}

	@Override
	public boolean isPrimitive() {
		return this.clazz.isPrimitive();
	}

	@Override
	public Class<?> componentType() {
		return this.clazz.componentType();
	}

	@Override
	public Class<?> arrayType() {
		return this.clazz.arrayType();
	}

	@Override
	public String descriptorString() {
		return this.clazz.descriptorString();
	}

	@Override
	public TypeVariable<?>[] getTypeParameters() {
		return this.clazz.getTypeParameters();
	}

	@Override
	public <U extends Annotation> U getAnnotation(@NotNull Class<U> annotationClass) {
		return this.clazz.getAnnotation(annotationClass);
	}

	@Override
	public @NotNull Annotation @NotNull [] getAnnotations() {
		return this.clazz.getAnnotations();
	}

	@Override
	public @NotNull Annotation @NotNull [] getDeclaredAnnotations() {
		return this.clazz.getDeclaredAnnotations();
	}
}
