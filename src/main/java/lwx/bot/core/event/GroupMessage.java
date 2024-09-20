package lwx.bot.core.event;

import cn.hutool.json.JSONObject;
import com.lwx.lwx.core.body.*;
import lwx.bot.core.body.*;
import lwx.bot.util.LwxUtil;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/21:15
 * @Description:
 */
@Getter
public class GroupMessage extends MessageBody {
    private final Long groupId;
    private final String subType;
    private final String anonymous;
    private final Sender sender;
    private final List<DataBody> message;


    public String text(){
        return LwxUtil.getText(message);
    }
    public GroupMessage(JSONObject jsonObject, Long groupId, Long selfId) {
        super();
        this.setSelfId(selfId);
        this.setPostType(jsonObject.getStr("post_type"));
        this.setMessageId(jsonObject.getLong("message_id"));
        this.setTime(new Date(jsonObject.getLong("time") * 1000));
        this.setRawMessage(jsonObject.getStr("raw_message"));
        this.groupId = groupId;
        this.subType = jsonObject.getStr("sub_type");
        this.anonymous = jsonObject.getStr("anonymous");
        this.sender = jsonObject.getJSONObject("sender").toBean(Sender.class);
        //消息体
        this.message = jsonObject.getJSONArray("message").toList(DataBody.class);
    }

    //发送消息
    public void sendText(@NonNull String text){
        DataBody e1 = new DataBody();
        e1.setType("text");
        SubData data = new SubData();
        data.setText(text);
        e1.setData(data);
        sendMessage(List.of(e1));
    }

    //at指定用户
    public void sendTextToUser(@NonNull String text,long userId){
        DataBody e1 = new DataBody();
        e1.setType("text");
        SubData data = new SubData();
        data.setText(text);
        e1.setData(data);
        DataBody e2 = new DataBody();
        e2.setType("at");
        SubData data1 = new SubData();
        data1.setQq(userId);
        e2.setData(data1);
        sendMessage(List.of(e1, e2));
    }
    //回复
    public void reply(@NonNull String text){
        sendTextToUser(text,sender.getUser_id());
    }

    public void sendFile(@NonNull String url){
        DataBody e1 = new DataBody();
        e1.setType("file");
        SubData data = new SubData();
        data.setFile(url);
        e1.setData(data);
    }

    public void sendImage(@NonNull String urlOrBase64){
        sendImage(List.of(urlOrBase64));
    }

    public void sendTextImage(@NonNull String urlOrBase64,@NonNull String text){
        sendTextImage(List.of(urlOrBase64),text);
    }

    public void sendTextImage(@NonNull List<String> urlOrBase64, String text){
        List<DataBody> list = new ArrayList<>(4);
        for (String s : urlOrBase64) {
            DataBody e1 = new DataBody();
            e1.setType("image");
            SubData data = new SubData();
            data.setFile(s.startsWith("http" )? s :"base64://"+s);
            e1.setData(data);
            list.add(e1);
        }
        if (text != null) {
            DataBody e2 = new DataBody();
            e2.setType("text");
            SubData data2 = new SubData();
            data2.setText(text);
            e2.setData(data2);
            list.add(e2);
        }
        sendMessage(list);
    }

    public void sendImage(@NonNull List<String> urlOrBase64){
        sendTextImage(urlOrBase64,null);
    }

    public void sendMessage(List<DataBody> list) {
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setMessage(list);
        super.sendMessage(body, "/send_group_msg");
    }

    public void setGroupKick(Long userId,boolean rejectAddReques){
        SendDataBody body = new SendDataBody();
        body.setUser_id(userId);
        body.setGroup_id(groupId);
        body.setReject_add_request(rejectAddReques);
        sendMessage(body,"/set_group_kick");
    }
    public void setGroupBan(Long userId,long duration){
        SendDataBody body = new SendDataBody();
        body.setUser_id(userId);
        body.setGroup_id(groupId);
        body.setDuration(duration);
        sendMessage(body,"/set_group_ban");
    }
    public void setGroupWholeBan(boolean enable){
        SendDataBody body = new SendDataBody();
        body.setEnable(enable);
        body.setGroup_id(groupId);
        sendMessage(body,"/set_group_whole_ban");
    }
    public void setGroupAdmin(boolean enable,Long userId){
        SendDataBody body = new SendDataBody();
        body.setEnable(enable);
        body.setUser_id(userId);
        body.setGroup_id(groupId);
        sendMessage(body,"/set_group_admin");
    }
    public void setGroupAnonymous(boolean enable){
        SendDataBody body = new SendDataBody();
        body.setEnable(enable);
        body.setGroup_id(groupId);
        sendMessage(body,"/set_group_anonymous");
    }
    public void setGroupCard(Long userId,String card){
        SendDataBody body = new SendDataBody();
        body.setCard(card);
        body.setUser_id(userId);
        body.setGroup_id(groupId);
        sendMessage(body,"/set_group_card");
    }
    public void setGroupName(String groupName){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setGroup_name(groupName);
        sendMessage(body,"/set_group_name");
    }

    /**
     *
     * @param isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     */
    public void setGroupLeave(boolean isDismiss){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setIs_dismiss(isDismiss);
        sendMessage(body,"/set_group_leave");
    }

    /**
     * @param userId 要设置的 QQ 号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @param duration 专属头衔有效期，单位秒，-1 表示永久，不过此项似乎没有效果，可能是只有某些特殊的时间长度有效，有待测试
     */
    public void setGroupSpecialTitle(Long userId,String specialTitle,long duration){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setUser_id(userId);
        body.setSpecial_title(specialTitle);
        body.setDuration(duration);
        sendMessage(body,"/set_group_special_title");
    }
    /**
     * 获取群信息
     */
    public GroupInfo getGroupInfo(boolean cache){
        return getGroupInfo(groupId, cache);
    }

    /**
     * 获取群成员信息
     */
    public GroupMemberInfo getGroupMemberInfo(long userId,boolean cache){
        return getGroupMemberInfo(groupId, userId, cache);
    }

    public List<GroupMemberInfo> getGroupMemberList(){
        return getGroupMemberList(groupId);
    }

    /**
     * 获取群荣誉信息 `talkative` `performer` `legend` `strong_newbie` `emotion` all为获取所有信息
     */
    public GroupHonorInfo getGroupHonorInfo(String type){
        return getGroupHonorInfo(groupId, type);
    }
}
