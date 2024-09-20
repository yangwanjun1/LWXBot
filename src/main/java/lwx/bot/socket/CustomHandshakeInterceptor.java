package lwx.bot.socket;

import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * 鉴权
 * @Author: ywj
 * @Date: 2024/08/24/21:57
 * @Description:
 */
@AllArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private final boolean isAuth;
    private final Set<String> tokenSet;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (!isAuth){
            return true;
        }
        // 这将获取整个查询字符串
        String paramValue = request.getURI().getQuery();
        // 在握手之前获取请求头信息
        String authorization = request.getHeaders().getFirst("Authorization");
//        attributes.put("authorization", headerValue);
        //鉴权
        if (tokenSet != null && !tokenSet.isEmpty()){
            return tokenSet.contains(paramValue) || tokenSet.contains(authorization);
        }
        //放行
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
