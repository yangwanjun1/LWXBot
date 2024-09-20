package lwx.bot.util;

import lwx.bot.core.body.DataBody;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/13/11:36
 * @Description:
 */
public class LwxUtil {

    public static boolean isAtMe(List<DataBody> messages, @NonNull Long selfId){
        return messages.stream().anyMatch(m -> m.getType().equals("at") && m.getData().getQq().equals(selfId));
    }

    public static boolean isAtAll(@NonNull List<DataBody> messages){
        return messages.stream().anyMatch(m -> "at".equals(m.getType()) && m.getData().getQq().equals(0L));
    }

    public static String getText(@NonNull List<DataBody> messages){
        StringBuilder sb = new StringBuilder();
        messages.stream().filter(m->m.getType().equals("text")).forEach(m->sb.append(m.getData().getText()));
        return sb.toString();
    }

    public static String getTextTrim(@NonNull List<DataBody> messages){
        StringBuilder sb = new StringBuilder();
        messages.stream().filter(m->m.getType().equals("text")).forEach(m->sb.append(m.getData().getText()));
        return sb.toString().trim();
    }

    public static List<String> getImage(@NonNull List<DataBody> messages){
        return messages.stream().filter(m->m.getType().equals("image")).map(m-> StringUtils.hasText(m.getData().getUrl()) ? m.getData().getUrl() : m.getData().getFile() ).collect(Collectors.toList());
    }

    public static List<String> getFile(@NonNull List<DataBody> messages){
        return messages.stream().filter(m->m.getType().equals("file")).map(m->m.getData().getFile()).collect(Collectors.toList());
    }



}
