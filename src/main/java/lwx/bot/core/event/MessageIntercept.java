package lwx.bot.core.event;

import lwx.bot.core.body.MessageBody;

/**
 * Created with IntelliJ IDEA.
 * 该拦截器仅拦截聊天信息，通知类型消息不做拦截
 * @Author: ywj
 * @Date: 2024/09/12/16:20
 * @Description:
 */
public interface MessageIntercept {
    /**
     * 进来之前
     * @return true:拦截 false:放行
     */
    boolean inBefore(MessageBody body);


}
