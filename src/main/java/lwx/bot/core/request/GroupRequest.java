package lwx.bot.core.request;

import cn.hutool.json.JSONObject;
import lwx.bot.core.body.MessageBody;
import lwx.bot.core.body.SendDataBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/9:02
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupRequest extends MessageBody {
    private String subType;
    private String requestType;
    private Long userId;
    private Long groupId;
    private String comment;
    private String flag;

    public GroupRequest(JSONObject jsonObject, Long selfId, Long groupId) {
        super();
        setSelfId(selfId);
        setPostType(jsonObject.getStr("post_type"));
        setTime(new Date(jsonObject.getLong("time")*1000L));
        this.subType = jsonObject.getStr("sub_type");
        this.requestType = jsonObject.getStr("request_type");
        this.userId = jsonObject.getLong("user_id");
        this.groupId = groupId;
        this.comment = jsonObject.getStr("comment");
        this.flag = jsonObject.getStr("flag");
    }


    /**
     *
     * @param approve 是否同意
     * @param reason 拒绝理由
     */
    public void handlerInviteOrAddRequest(boolean approve,String reason){
        SendDataBody body = new SendDataBody();
        body.setFlag(flag);
        body.setSub_type(subType);
        body.setReason(reason);
        body.setApprove(approve);
        sendMessage(body,"set_group_add_request");
    }

}
