package lwx.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/14:48
 * @Description:
 */
@ConfigurationProperties(prefix = "lwx")
@Component
@Data
public class LwxProperty {
    private String host = "127.0.0.1";
    private Integer port= 8082;
    private Integer wsPort = 8081;
    private Boolean client = true;
    private String protocol = "http";
    private Boolean intercept = false;
    private Boolean enabledAuthConnection = false;
    private String token;
    private String serverPath = "/ws";
    /**
     * 核心数
     */
    private int threadPollCore = 2;
    /***
     * 最大线程数
     */
    private int threadPollMaxSize = 8;
    /**
     * 存活时间
     */
    private int threadKeepAliveTime = 30;
    /**
     * 队列容量
     */
    private int threadPollBlockSize = 100;
}
