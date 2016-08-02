package me.zouooh.gum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Views
 * @author zouooh
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Gum {
	/**
	 * 资源Id.自动绑定View时用动
	 * @return
	 */
	int resId() default 0;
	
}
