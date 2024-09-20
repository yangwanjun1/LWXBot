package lwx.bot.core.body;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/21:40
 * @Description:
 */
@Data
public class Sender {
    private Long user_id;
    private String nickname;
    private String card;
    private String sex;
    private Integer age;
    private String area;
    private String level;
    private String role;
    private String title;
    private String q_id;
    private String remark;

}
