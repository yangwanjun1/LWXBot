package lwx.bot.core.body;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/11:47
 * @Description:
 */
@Data
public class CurrentTalkative {
     private Long uin;
     private Long day_count;
     private String avatar;
     private Long avatar_size;
     private String nick;
}
