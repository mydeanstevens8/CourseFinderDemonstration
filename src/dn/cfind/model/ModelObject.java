package dn.cfind.model;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ModelObject {
}
