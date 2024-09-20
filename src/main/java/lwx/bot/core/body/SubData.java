package lwx.bot.core.body;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/21:44
 * @Description:
 */
@Data
public class SubData {
    private Long qq;
    private String name;
    private String text;
    private String file;
    private String url;
    private String uni;
    private String summary;
    private String subType;
    private List<SubData> content;
}
