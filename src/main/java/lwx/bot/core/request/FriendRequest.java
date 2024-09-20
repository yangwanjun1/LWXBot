package lwx.bot.core.request;

import cn.hutool.json.JSONObject;
import lwx.bot.core.body.MessageBody;
import lwx.bot.core.body.SendDataBody;
import lombok.Getter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/8:53
 * @Description:
 */
@Getter
public class FriendRequest extends MessageBody {
    private final Long selfId;
    private final Long requestType;
    private final Long userId;
    private final String comment;
    private final String flag;

    public FriendRequest(JSONObject jsonObject, Long selfId) {
        super();
        setTime(new Date(jsonObject.getLong("time") * 1000));
        setPostType(jsonObject.getStr("post_type"));
        requestType = jsonObject.getLong("request_type");
        userId = jsonObject.getLong("user_id");
        comment = jsonObject.getStr("comment");
        flag = jsonObject.getStr("flag");
        this.selfId = selfId;
    }

    /**
     *
     * @param approve 是否同意
     * @param remark 添加后的好友备注（仅在同意时有效）
     */
    public void handlerAddRequest(boolean approve,String remark){
        SendDataBody body = new SendDataBody();
        body.setApprove(approve);
        body.setRemark(remark);
        body.setFlag(flag);
        sendMessage(body,"set_friend_add_request");
    }

}
