package lgbt.greenhouse.silicate.impl.annotation;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@TypeQualifierDefault(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Nonnull
public @interface NonnullByDefault {
}
