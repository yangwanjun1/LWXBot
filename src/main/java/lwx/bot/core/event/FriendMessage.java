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
 * @Date: 2024/09/13/9:59
 * @Description:
 */
@Getter
public class FriendMessage extends MessageBody {
    private final Long userId;
    private final Sender sender;
    private final List<DataBody> message;
    private final String subType;
    private final Long targetId;
    public String text(){
        return LwxUtil.getText(message);
    }
    public FriendMessage(JSONObject jsonObject, Long selfId) {
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
    }


    //发送消息
    public void sendTextToUser(@NonNull String text,Long userId){
        DataBody e2 = new DataBody();
        e2.setType("text");
        SubData data1 = new SubData();
        data1.setText(text);
        e2.setData(data1);
        sendMessage(List.of(e2),userId);
    }

    public void sendFile(@NonNull String url,Long userId){
        DataBody e1 = new DataBody();
        e1.setType("file");
        SubData data = new SubData();
        data.setFile(url);
        e1.setData(data);
        sendMessage(List.of(e1),userId);
    }

    public void sendImage(@NonNull String urlOrBase64,Long userId){
        sendTextImage(List.of(urlOrBase64),null,userId);
    }

    public void sendTextImage(@NonNull String urlOrBase64,@NonNull String text,Long userId){
        sendTextImage(List.of(urlOrBase64),text,userId);
    }

    public void sendTextImage(@NonNull List<String> urlOrBase64, String text,Long userId){
        List<DataBody> list = new ArrayList<>();
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
        sendMessage(list,userId);
    }

    public void sendImage(@NonNull List<String> urlOrBase64,Long userId){
        sendTextImage(urlOrBase64,null,userId);
    }

    public void sendMessage(List<DataBody> list,Long userId){
        SendDataBody body = new SendDataBody();
        body.setUser_id(userId == null ? this.userId : userId);
        body.setMessage(list);
        sendMessage(body,"/send_private_msg");
    }
}


