package lwx.bot.core.body;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/14/11:36
 * @Description:
 */
@Data
public class GroupHonorInfo {
    private CurrentTalkative current_talkative;
    private List<TalkativeList> talkative_list;
    private List<CurrencyHonorList> legend_list;
    private List<CurrencyHonorList> strong_newbie_list;
    private List<CurrencyHonorList> emotion_list;
}
