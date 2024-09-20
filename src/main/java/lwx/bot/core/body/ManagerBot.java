package lwx.bot.core.body;



import lwx.bot.core.Bot;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/16:50
 * @Description:
 */

public class ManagerBot {
    private static final Map<Long, Bot> botMap = new ConcurrentHashMap<>(4);

    public static void add(Long selfId,Bot bot){
        botMap.put(selfId,bot);
    }
    public static Bot get(Long selfId){
        return botMap.get(selfId);
    }

    public static Collection<Bot> getAll(){
        return botMap.values();
    }
    public static boolean hasBot(Long selfId){
        return botMap.containsKey(selfId);
    }
}
