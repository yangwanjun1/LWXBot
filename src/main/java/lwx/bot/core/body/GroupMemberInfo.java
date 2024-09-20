package lwx.bot.core.body;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/11:31
 * @Description:
 */
@Data
public class GroupMemberInfo {
    private Long group_id;
    private Long user_id;
    //年龄
    private Long age;
    //加群时间戳
    private Long join_time;
    //最后发言时间戳
    private Long last_sent_time;
    //昵称
    private String nickname;
    //群名片／备注
    private String card;
    //成员等级
    private String level;
    //角色，`owner` 或 `admin` 或 `member`
    private String role;
    //是否不良记录成员
    private String unfriendly;
    //专属头衔
    private String title;
    //专属头衔过期时间戳
    private Long title_expire_time;
    //性别，`male` 或 `female` 或 `unknown`
    private String sex;
    //地区
    private String area;
    //是否允许修改群名片
    private Boolean card_changeable;
}
