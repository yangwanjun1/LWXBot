package lwx.bot.core.body;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/11:28
 * @Description:
 */
@Data
public class GroupInfo {
    private Long group_id;
    private String group_name;
    private Long member_count;
    private Long max_member_count;
}
