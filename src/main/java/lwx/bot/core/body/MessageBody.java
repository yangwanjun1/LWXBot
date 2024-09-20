package lwx.bot.core.body;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/16:21
 * @Description:
 */
@Data
@Slf4j
public class MessageBody {
    /**
     * 可判断事件是私聊还是群聊进行处理
     */
    private String postType;
    private String rawMessage;
    private Date time;
    private Long selfId;
    private Long messageId;

    public void setMessageId(Long messageId) {
        if (this.messageId != null){
            log.error("messageId不可设置");
            return;
        }
        this.messageId = messageId;
    }
    public void setPostType(String postType) {
        if (this.postType != null){
            log.error("postType不可设置");
            return;
        }
        this.postType = postType;
    }

    public void setSelfId(Long selfId) {
        if (this.selfId != null){
            log.error("selfId 不可设置");
            return;
        }
        this.selfId = selfId;
    }

    protected String sendMessage(SendDataBody body, String path){
        return ManagerBot.get(this.getSelfId()).request(body,path);
    }
    public Message getMessageById(Long id){
        SendDataBody body = new SendDataBody();
        body.setMessage_id(id);
        String s = sendMessage(body, "/get_msg");
        return s != null ? JSONUtil.toBean(JSONUtil.parseObj(s).getJSONObject("data"), Message.class) : null;
    }

    /**
     * message 中的id ,自己解析。太懒了
     */
    public String getForwardMsg(Long id){
        SendDataBody body = new SendDataBody();
        body.setId(id);
        return sendMessage(body,"/get_forward_msg");
    }
    public void sendLike(Long userId,Long times){
        SendDataBody body = new SendDataBody();
        body.setUser_id(userId);
        body.setTimes(times == null ? 1 : times);
        sendMessage(body,"/send_like");
    }
    public Sender getStrangerInfo(Long userId,boolean cache){
        SendDataBody body = new SendDataBody();
        body.setUser_id(userId);
        body.setNo_cache(cache);
        String s = sendMessage(body, "/get_stranger_info");
        if (s != null){
            return JSONUtil.toBean(s,Sender.class);
        }
        return null;
    }
    public List<Sender> getFriendList(){
        String s = sendMessage(null, "/get_friend_list");
        if (s != null){
            return JSONUtil.toList(s,Sender.class);
        }
        return null;
    }
    public GroupInfo getGroupInfo(long groupId,boolean cache){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setNo_cache(cache);
        String s = sendMessage(body, "/get_group_info");
        return s != null ? JSONUtil.toBean(JSONUtil.parseObj(s).getJSONObject("data"), GroupInfo.class) : null;
    }
    public GroupMemberInfo getGroupMemberInfo(long groupId,long userId,boolean cache){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setNo_cache(cache);
        body.setUser_id(userId);
        String s = sendMessage(body, "/get_group_member_info");
        return s == null ? null : JSONUtil.toBean(JSONUtil.parseObj(s).getJSONObject("data"), GroupMemberInfo.class);
    }
    public List<GroupMemberInfo> getGroupMemberList(long groupId){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        String list = sendMessage(body, "/get_group_member_list");
        return list != null ? JSONUtil.toList(JSONUtil.parseObj(list).getJSONArray("data"), GroupMemberInfo.class) : null;
    }

    /**
     *
     * @param type 要获取的群荣誉类型，可传入 talkative performer legend strong_newbie emotion 以分别获取单个类型的群荣誉数据，或传入 all 获取所有数据
     * @return
     */
    public GroupHonorInfo getGroupHonorInfo(long groupId,String type){
        SendDataBody body = new SendDataBody();
        body.setGroup_id(groupId);
        body.setType(type);
        String info = sendMessage(body, "/get_group_honor_info");
        return info != null ? JSONUtil.toBean(JSONUtil.parseObj(info).getJSONObject("data"), GroupHonorInfo.class) : null;
    }

    public String getVersionInfo(){
        return sendMessage(null,"/get_version_info");
    }
    public String getStatus(){
        return sendMessage(null,"/get_status");
    }

    public void cleanCache(){
        sendMessage(null,"/clean_cache");
    }

    public void setRestart(long delay){
        SendDataBody body = new SendDataBody();
        body.setDuration(delay);
        sendMessage(body,"/set_restart");
    }

    /**
     *
     * @param list 消息列表
     * @return 返回id
     */
    public String createForwardMessage(List<DataBody> list){
        SendDataBody body = new SendDataBody();
        body.setMessage(list);
        String s = sendMessage(body, "/send_forward_msg");
        return s == null ? null : JSONUtil.parseObj(s).getJSONObject("data").getStr("forward_id");
    }

}

