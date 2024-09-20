package lwx.bot.core.event;

import cn.hutool.json.JSONObject;
import lwx.bot.core.body.DataBody;
import lwx.bot.core.body.MessageBody;
import lwx.bot.core.body.SendDataBody;
import lwx.bot.core.body.SubData;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/18:09
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public  class NoticeEvent extends MessageBody {
    private final Long groupId;
    private final Long operatorId;
    private final Long userId;
    private final Long targetId;
    private final String noticeType;
    //kick_me 机器人被踢  approve、invite分别表示管理员已同意入群、管理员邀请入群
    private final String subType;
    private final String suffix;
    //talkative、performer、emotion	荣誉类型，分别表示龙王、群聊之火、快乐源泉
    private final String honorType;
    //单位秒
    private final Long duration;
    private final String action;
    private final String file;
    public NoticeEvent(JSONObject jsonObject, Long selfId, Long groupId){
        this.setSelfId(selfId);
        Long time = jsonObject.getLong("time");
        this.setTime(time == null ?new Date() : new Date(time));
        this.setPostType("notice");
        this.setMessageId(jsonObject.getLong("message_id"));
        this.operatorId = jsonObject.getLong("operator_id");
        this.groupId = groupId;
        this.userId = jsonObject.getLong("user_id");
        this.subType=jsonObject.getStr("sub_type");
        this.noticeType=jsonObject.getStr("notice_type");
        this.action=jsonObject.getStr("action");
        this.suffix=jsonObject.getStr("suffix");
        this.duration=jsonObject.getLong("duration");
        this.targetId=jsonObject.getLong("target_id");
        this.honorType = jsonObject.getStr("honor_type");
        this.file = jsonObject.getStr("file");
    }
    public void sendGroupMessage(String text,Long groupId){
        SendDataBody body = new SendDataBody();

        DataBody e1 = new DataBody();
        e1.setType("text");
        SubData data = new SubData();
        data.setText(text);
        e1.setData(data);

        body.setGroup_id(groupId);
        body.setMessage(List.of(e1));
        sendMessage(body,"send_group_msg");

    }

}
