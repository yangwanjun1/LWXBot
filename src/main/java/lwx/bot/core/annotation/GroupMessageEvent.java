package lwx.bot.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/13/8:44
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupMessageEvent {
    /**
     * 正则
     */
    String matcher() default "";

    /**
     * 监听群号，0不监听
     */
    long listenerGroup() default  -1;

    /**
     * 是否监听@我，与atAll互斥
     * @return
     */
    boolean atMe() default false;

    /**
     * 是否监听@所有人，与atMe互斥
     * @return
     */
    boolean atAll() default false;
}
