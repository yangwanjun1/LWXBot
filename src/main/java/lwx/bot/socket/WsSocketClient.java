package lwx.bot.socket;

import lwx.bot.envet.OneBotEventHandler;
import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/14:40
 * @Description:
 */

@Slf4j
@ClientEndpoint
public class WsSocketClient {
    private volatile Timer timer ;
    private final AtomicInteger count = new AtomicInteger(0);
    private final OneBotEventHandler oneBotEventHandler;

    private final ThreadPoolExecutor poolExecutor;
    private Session session ;
    private final String host;
    private final String path;
    private final int port;
    private final boolean isAuth;
    private final String token;

    public WsSocketClient(String host, int port, OneBotEventHandler oneBotEventHandler,String path,ThreadPoolExecutor poolExecutor,boolean isAuth,String token) {
        this.host = host;
        this.port = port;
        this.oneBotEventHandler = oneBotEventHandler;
        this.poolExecutor = poolExecutor;
        this.path = path;
        this.isAuth = isAuth;
        this.token = token;
    }

    public WsSocketClient init() throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(1024*1000);
        container.setDefaultMaxTextMessageBufferSize(1024*1000);
        String ws = "ws://"+host+":"+port+path;
        session = container.connectToServer(this, new URI(isAuth ? ws : ws+"?access_token="+token));
        return this;
    }

    public void close() throws IOException {
        session.close();
    }

    @OnOpen
    public void onOpen(Session session){
        if (timer != null){
            count.set(0);
            timer.cancel();
            timer = null;
        }
        if (isAuth){
            session.getAsyncRemote().sendText("Authorization: Bearer "+token);
        }
    }

    @OnMessage
    public void onMessage(String message,Session session){
        poolExecutor.execute(() -> oneBotEventHandler.handler(message,token));
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.error("连接关闭");
    }

    @OnError
    public void onError(Throwable e){
        log.error("连接错误:{},正在尝试连接...",e.getMessage());
        restConnect();
    }

    public synchronized void restConnect(){
        if (timer == null){
            timer = new Timer("wsRestConnected");
            if (count.intValue() > 1000){
                log.info("重连次数达到限制，ws连接已经断开");
                return;
            }
            count.addAndGet(1);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        init();
                    } catch (DeploymentException | IOException | URISyntaxException e) {
                        log.error("error:{}",e.getMessage());
                    }
                }
            },1000, 5000L);
        }
    }
}
