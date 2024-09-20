package lwx.bot.core.event;

import cn.hutool.json.JSONObject;
import com.lwx.lwx.core.body.*;
import lwx.bot.core.body.*;
import lwx.bot.util.LwxUtil;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/13/10:56
 * @Description:
 */
@Getter
public class TemporaryMessage extends MessageBody {
    private final Long targetId;
    private final String subType;
    private final Long userId;
    private final Long groupId;
    private final Sender sender;
    private final List<DataBody> message;
    public String text(){
        return LwxUtil.getText(message);
    }
    public TemporaryMessage(JSONObject jsonObject,  Long selfId) {
        super();
        this.setSelfId(selfId);
        this.setPostType(jsonObject.getStr("post_type"));
        this.setMessageId(jsonObject.getLong("message_id"));
        this.setTime(new Date(jsonObject.getLong("time") * 1000));
        this.setRawMessage(jsonObject.getStr("raw_message"));
        this.subType = jsonObject.getStr("sub_type");
        this.userId = jsonObject.getLong("user_id");
        this.targetId = jsonObject.getLong("target_id");
        this.sender = jsonObject.getJSONObject("sender").toBean(Sender.class);
        this.message = jsonObject.getBeanList("message", DataBody.class);
        this.groupId = jsonObject.getLong("group_id");
    }
    public void reply(String text){
        sendText(text,groupId,userId);
    }
    public void sendText(String text,Long groupId,Long userId){
        SendDataBody body = new SendDataBody();
        body.setType("private");
        body.setGroup_id(groupId);
        body.setUser_id(userId);
        DataBody dataBody = new DataBody();
        dataBody.setType("text");
        SubData data = new SubData();
        data.setText(text);
        dataBody.setData(data);
        body.setMessage(List.of(dataBody));
        sendMessage(body);
    }
    private void sendMessage(SendDataBody body){
        ManagerBot.get(this.getSelfId()).request(body,"send_msg");
    }
}
