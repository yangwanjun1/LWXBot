package lwx.bot.core;

import cn.hutool.json.JSONUtil;
import lwx.bot.core.body.SendDataBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/16:51
 * @Description:
 */
@Slf4j
@AllArgsConstructor
public class Bot {
    @Getter
    private Long selfId;
    private String host;
    private Integer port;
    @Getter
    private Date loginDate;
    private String protocol;
    private String token;
    public String request(SendDataBody bodyData, String path){
        HttpResponse<String> send = null;
        try {
            String text = bodyData == null ? "{}" : JSONUtil.toJsonStr(bodyData);
            URI uri = new URI(protocol + "://" + host + ":" + port +  (path.startsWith("/") ? path : "/"+path));
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json;charset=utf-8")
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofSeconds(30));
            if (token!= null){
                builder.header("Authorization",token.startsWith("Bearer") ? token : "Bearer "+token);
            }
            HttpRequest request = bodyData == null ?
                    builder.POST(HttpRequest.BodyPublishers.noBody()).build()
                    :builder.POST(HttpRequest.BodyPublishers.ofString(text)).build();
            send = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (send.statusCode() != 200){
                log.info("发送消息失败：{}",send.body());
            }else {
                return send.body();
            }
        } catch (Exception e) {
            log.error("发送消息出现异常：{}",e.getMessage());
        }
        return null;
    }


}
