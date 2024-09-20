package lwx.bot.core.body;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/11:58
 * @Description:
 */
@Data
public class Message {
    private Long time;
    private String message_type;
    private Long message_id;
    private Long real_id;
    private Sender sender;
    private List<DataBody> message;
}
