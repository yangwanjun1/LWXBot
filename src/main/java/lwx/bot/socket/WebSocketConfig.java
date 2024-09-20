package lwx.bot.socket;

import lwx.bot.config.LwxProperty;
import lwx.bot.envet.OneBotEventHandler;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/08/24/21:53
 * @Description:
 */
@Configuration
@EnableWebSocket
@AllArgsConstructor
@ConditionalOnProperty(prefix = "lwx", name = "client", havingValue = "false")
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource(name = "lwxProperty")
    private LwxProperty property;

    @Resource(name = "customThreadPoolExecutor")
    private ThreadPoolExecutor poolExecutor;

    @Resource(name = "oneBotEventHandler")
    private OneBotEventHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        handler.setPort(property.getPort());
        handler.setIntercept(property.getIntercept());
        handler.setProtocol(property.getProtocol());
        registry.addHandler(new MyWebSocketHandler(poolExecutor,handler), property.getServerPath())
                .addInterceptors(new CustomHandshakeInterceptor(property.getEnabledAuthConnection(), Set.of(property.getToken().split(",")).stream().filter(StringUtils::hasText).collect(Collectors.toSet())))
                .setAllowedOrigins("*");
    }

}

