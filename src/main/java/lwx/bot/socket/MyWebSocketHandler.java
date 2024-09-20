package lwx.bot.socket;

import lwx.bot.envet.OneBotEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/08/24/21:54
 * @Description:
 */
@AllArgsConstructor
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {

    private final ThreadPoolExecutor poolExecutor;

    @Getter
    private final OneBotEventHandler handler;
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        poolExecutor.execute(() -> {
            try {
                handler.handlerServer( session,message);
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error("异常信息:{}",e.getMessage());
            }
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("{}=>连接断开:{}",session.getRemoteAddress(),exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
