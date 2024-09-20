package lwx.bot.core.body;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/13/19:52
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendDataBody {
    private Long group_id;
    private Long user_id;
    private Long message_id;
    private Long id;
    //点赞次数 默认=1
    private Long times;
    //禁言时间
    private Long duration;
    //	拒绝此人的加群请求
    private Boolean reject_add_request;
    private Boolean auto_escape ;
    //群组全员禁言
    private Boolean enable ;
    //解散群
    private Boolean is_dismiss ;
    //是否同意请求／邀请
    private Boolean approve ;
    //名片
    private String card;
    private String group_name;
    private String nickname;
    //是否不使用缓存（使用缓存可能更新不及时，但响应更快）
    private Boolean no_cache;
    private String remark;
    //要转换到的格式，目前支持 mp3、amr、wma、m4a、spx、ogg、wav、flac
    private String out_format;
    private String file;
    //要延迟的毫秒数，如果默认情况下无法重启，可以尝试设置延迟为 2000 左右
    private Long delay;
    //专属头衔，不填或空字符串表示删除专属头衔
    private String special_title;
    private String message_type ;
    private String sub_type ;
    //拒绝理由（仅在拒绝时有效）
    private String reason ;
    private String type ;
    private String anonymous ;
    private String anonymous_flag  ;
    private String flag ;
    private List<DataBody> message;

}
