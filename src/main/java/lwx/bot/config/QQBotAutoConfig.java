package lwx.bot.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnMissingBean(QQBotAutoConfig.class)
@ComponentScan(basePackages = "lwx.bot")
public class QQBotAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public QQBotAutoConfig autoConfig(){
        return new QQBotAutoConfig();
    }

}
