package lwx.bot.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/21:34
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface LwxBot {
}
