package lwx.bot.config;

import lwx.bot.envet.OneBotEventHandler;
import lwx.bot.socket.WsSocketClient;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/14:45
 * @Description:
 */
@Configuration
public class InitConfig {

    @Resource(name = "lwxProperty")
    private LwxProperty property;

    @Resource(name = "oneBotEventHandler")
    private OneBotEventHandler handler;

    @ConditionalOnProperty(prefix = "lwx", name = "client", havingValue = "true")
    @Bean(destroyMethod = "close",initMethod = "init")
    public WsSocketClient wsSocketClient()  {
        handler.setPort(property.getPort());
        handler.setIntercept(property.getIntercept());
        handler.setHost(property.getHost());
        handler.setProtocol(property.getProtocol());
        return new WsSocketClient(property.getHost(),property.getWsPort(),handler,property.getServerPath(),customThreadPoolExecutor(), property.getEnabledAuthConnection(),property.getToken());
    }


    @Bean
    public ThreadPoolExecutor customThreadPoolExecutor(){
        return new ThreadPoolExecutor(property.getThreadPollCore()
                , property.getThreadPollMaxSize()
                , property.getThreadKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(property.getThreadPollBlockSize()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
