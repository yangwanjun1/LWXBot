package lwx.bot.envet;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lwx.bot.core.annotation.*;
import lwx.bot.core.body.ManagerBot;
import lwx.bot.core.body.MessageBody;
import lwx.bot.core.event.NoticeEvent;
import lwx.bot.core.event.FriendMessage;
import lwx.bot.core.event.GroupMessage;
import lwx.bot.core.event.MessageIntercept;
import lwx.bot.core.event.TemporaryMessage;
import lwx.bot.core.request.FriendRequest;
import lwx.bot.core.request.GroupRequest;
import lwx.bot.util.LwxUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lwx.bot.core.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ywj
 * @Date: 2024/09/12/14:44
 * @Description:
 */
@Setter
@Slf4j
@Component
public class OneBotEventHandler {

    private int port;
    private boolean intercept;
    private String host;
    @Setter
    private String protocol;

    @Autowired
    private ApplicationContext context;
    /**
     * class 注解类
     * map obj ：bean method：方法
     */
    private Map<Class<?>, Map<Method,Object>> eventMap;
    /**
     * 作为客户端时
     */
    public void handler(String message,String token) {
        try {
            handlerMessage(message,host,token);
        }catch (Exception e){
            e.printStackTrace();
            log.error("异常信息：{}",e.getMessage());
        }
    }

    public void handlerServer(WebSocketSession session, WebSocketMessage<?> message) throws InvocationTargetException, IllegalAccessException {
        handlerMessage(message.getPayload().toString(), Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress(),null);
    }

    private void handlerMessage(String message,String ip,String token) throws InvocationTargetException, IllegalAccessException {
        JSONObject jsonObject = JSONUtil.parseObj(message);
        //作为server，存在manageBot
        String object = jsonObject.getStr("sub_type");
        Long selfId = jsonObject.getLong("self_id");
        Long groupId = jsonObject.getLong("group_id");
        //存放bot信息
        if (!ManagerBot.hasBot(selfId) && object != null && object.equals("connect")) {
            ManagerBot.add(selfId, new Bot(selfId,ip,port,new Date(jsonObject.getLong("time") * 1000),protocol,token));
            log.info("QQ{}连接进来了，当前在线bot：{}",selfId,ManagerBot.getAll().size());
            return;
        }
        if (eventMap == null){
            eventMap  = new ConcurrentHashMap<>(8);
            Map<String, Object> beans = context.getBeansWithAnnotation(LwxBot.class);
            if (beans.isEmpty()){
                return;
            }
            initEvent(beans);
        }
        if (eventMap.isEmpty()){
            return;
        }
        String postType = jsonObject.getStr("post_type");
        switch (postType){
            case "notice"-> handlerNotice(jsonObject,selfId,groupId);
            case "message"->handlerMessage(jsonObject,selfId,groupId);
            case "request"->handlerRequest(jsonObject,selfId,groupId);
        }
    }

    private void handlerRequest(JSONObject jsonObject, Long selfId, Long groupId) throws InvocationTargetException, IllegalAccessException {
        String requestType = jsonObject.getStr("request_type");
        switch (requestType){
            case "friend"-> invokeNoticeOrRequest(new FriendRequest(jsonObject,selfId), FriendMessageEvent.class);
            case "group"-> invokeNoticeOrRequest(new GroupRequest(jsonObject,selfId,groupId),GroupRequestEvent.class);
            default -> {}
        }
    }

    private void initEvent(Map<String, Object> beans) {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object entryValue = entry.getValue();
            Method[] methods = entryValue.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PokeEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(PokeEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(PokeEvent.class,map);
                } else if (method.isAnnotationPresent(BanEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(BanEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(BanEvent.class,map);
                } else if (method.isAnnotationPresent(GroupMessageEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(GroupMessageEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(GroupMessageEvent.class,map);
                } else if (method.isAnnotationPresent(InviteGroupEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(InviteGroupEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(InviteGroupEvent.class,map);
                } else if (method.isAnnotationPresent(KickGroupEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(KickGroupEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(KickGroupEvent.class,map);
                } else if (method.isAnnotationPresent(LeaveGroupEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(LeaveGroupEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(LeaveGroupEvent.class,map);
                } else if (method.isAnnotationPresent(LiftBanEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(LiftBanEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(LiftBanEvent.class,map);
                } else if (method.isAnnotationPresent(SetGroupAdminEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(SetGroupAdminEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(SetGroupAdminEvent.class,map);
                } else if (method.isAnnotationPresent(TemporaryMessageEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(TemporaryMessageEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(TemporaryMessageEvent.class,map);
                } else if (method.isAnnotationPresent(UnsetGroupAdminEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(UnsetGroupAdminEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(UnsetGroupAdminEvent.class,map);
                } else if (method.isAnnotationPresent(FriendMessageEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(FriendMessageEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(FriendMessageEvent.class,map);
                } else if (method.isAnnotationPresent(GroupRecallEvent.class)) {
                    Map<Method, Object> map = eventMap.getOrDefault(GroupRecallEvent.class,new HashMap<>(4));
                    map.put(method,entryValue);
                    eventMap.put(GroupRecallEvent.class,map);
                }
            }
        }
    }

    private void handlerNotice(JSONObject jsonObject, Long selfId, Long groupId) throws InvocationTargetException, IllegalAccessException {
        NoticeEvent event = new NoticeEvent(jsonObject,selfId,groupId);
        String noticeType = jsonObject.getStr("sub_type");
        if (noticeType == null){
            noticeType = jsonObject.getStr("notice_type");
        }
        switch (noticeType){
            //戳一戳事件
            case "poke"-> invokeNoticeOrRequest(event, PokeEvent.class);
            //退群事件
            case "leave"-> invokeNoticeOrRequest(event, LeaveGroupEvent.class);
            //踢人事件
            case "kick","kick_me"-> invokeNoticeOrRequest(event, KickGroupEvent.class);
            //进群事件
            case "invite","approve"-> invokeNoticeOrRequest(event, InviteGroupEvent.class);
            //禁言事件
            case "ban"-> invokeNoticeOrRequest(event, BanEvent.class);
            //解除禁言
            case "lift_ban"-> invokeNoticeOrRequest(event, LiftBanEvent.class);
            //设置管理员
            case "set"-> invokeNoticeOrRequest(event, SetGroupAdminEvent.class);
            //取消管理员
            case "unset"-> invokeNoticeOrRequest(event, UnsetGroupAdminEvent.class);
            //撤回消息
            case "group_recall"-> invokeNoticeOrRequest(event, GroupRecallEvent.class);
            case "friend_add"->{}
            //运气王
            case "lucky_king"->{}
            //荣誉变更
            case "honor"->{}
            case "group_upload"->{}
            default -> {}
        }
    }

    private void invokeNoticeOrRequest(MessageBody event, Class<? extends Annotation> cls) throws InvocationTargetException, IllegalAccessException {
        Map<Method, Object> map = eventMap.getOrDefault(cls,new HashMap<>());
        for (Map.Entry<Method, Object> entry : map.entrySet()) {
            Method key = entry.getKey();
            if (key.isAnnotationPresent(cls)){
                key.invoke(entry.getValue(),loadParams(key,event,null));
                break;
            }
        }
    }
    private Object[] loadParams(Method m, MessageBody event, Matcher matcher) {
        Parameter[] parameters = m.getParameters();
        int length = parameters.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            Class<?> type = parameters[i].getType();
            if (NoticeEvent.class.isAssignableFrom(type)) {
                objects[i] = event;
            }
            else if (FriendMessage.class.isAssignableFrom(type)) {
                objects[i] = event;
            }
            else if (GroupMessage.class.isAssignableFrom(type)) {
                objects[i] = event;
            }
            else if (TemporaryMessage.class.isAssignableFrom(type)) {
                objects[i] = event;
            } else if (Matcher.class.equals(type)) {
                objects[i] = matcher;
            } else {
                objects[i] = null;
            }
        }
        return objects;
    }



    private void handlerMessage(JSONObject jsonObject, Long selfId, Long groupId) throws InvocationTargetException, IllegalAccessException {
        MessageBody body = null;
        String text = null;
        String messageType = jsonObject.getStr("message_type");
        String subType = jsonObject.getStr("sub_type");
        switch (messageType){
            case "private"->{
                if ("friend".equals(subType)) {
                    //好友消息
                    FriendMessage message = new FriendMessage(jsonObject, selfId);
                    body = message;
                    text = LwxUtil.getTextTrim(message.getMessage());
                } else if ("group".equals(subType)) {
                    //临时消息
                    TemporaryMessage message = new TemporaryMessage(jsonObject, selfId);
                    body = message;
                    text = LwxUtil.getTextTrim(message.getMessage());
                }
            }
            case "group"->{
                if ("normal".equals(subType)) {
                    //普通消息
                    GroupMessage message = new GroupMessage(jsonObject, groupId, selfId);
                    body = message;
                    text = LwxUtil.getText(message.getMessage());
                }
            }
            case "other"->{}
            default -> {}
        }
        if (body == null || intercept(body)){
            return;
        }
        invokeMessage(body,text);
    }

    private void invokeMessage(MessageBody body, String text) throws InvocationTargetException, IllegalAccessException {
        if (body instanceof GroupMessage g){
            Map<Method, Object>  map = eventMap.getOrDefault(GroupMessageEvent.class,new HashMap<>());
            //优先匹配指定监听群
            Map<Method, Object> listenerGroup =new HashMap<>();
            map.entrySet().stream().filter(e -> e.getKey().getAnnotation(GroupMessageEvent.class) != null && g.getGroupId().equals(e.getKey().getAnnotation(GroupMessageEvent.class).listenerGroup()))
                    .forEach(e-> listenerGroup.put(e.getKey(),e.getValue()));
            if (!listenerGroup.isEmpty() && execGroup(listenerGroup,body,text)){
                return;
            }
            //不符合直接匹配不指定的qq群
            Map<Method, Object> group = new HashMap<>();
                    map.entrySet().stream().filter(e -> e.getKey().getAnnotation(GroupMessageEvent.class) != null && e.getKey().getAnnotation(GroupMessageEvent.class).listenerGroup() == -1)
                    .forEach(e->group.put(e.getKey(),e.getValue()));
            execGroup(group,body,text);
        } else if (body instanceof FriendMessage){
            Map<Method, Object> map = eventMap.getOrDefault(FriendMessageEvent.class,new HashMap<>(4));
            execPrivate(map,body,text,1);
        }else {
            Map<Method, Object> map = eventMap.getOrDefault(TemporaryMessageEvent.class,new HashMap<>(4));
            execPrivate(map,body,text,2);
        }
    }

    private void execPrivate(Map<Method, Object> group, MessageBody body, String text,int type) throws InvocationTargetException, IllegalAccessException {
        if (group.isEmpty()){
            return;
        }
        Map<Method, Object> map = new HashMap<>(group.size());
        if (type == 1){
            group.entrySet().stream().filter(e ->!e.getKey().getAnnotation(FriendMessageEvent.class).matcher().isEmpty())
                    .forEach(e->map.put(e.getKey(),e.getValue()));
        }else{
            group.entrySet().stream().filter(e ->e.getKey().getAnnotation(TemporaryMessageEvent.class).matcher().isEmpty())
                    .forEach(e->map.put(e.getKey(),e.getValue()));
        }
        if (!map.isEmpty() && invokePrivate(map, body, text, type)) {
            return ;
        }
        if (type == 1){
            group.entrySet().stream().filter(e ->e.getKey().isAnnotationPresent(FriendMessageEvent.class))
                    .forEach(e->map.put(e.getKey(),e.getValue()));
        }else{
            group.entrySet().stream().filter(e ->e.getKey().isAnnotationPresent(TemporaryMessageEvent.class))
                    .forEach(e->map.put(e.getKey(),e.getValue()));
        }
        invokePrivate(group,body,text,type);
    }

    private boolean invokePrivate(Map<Method, Object> group, MessageBody body, String text, int type) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Method, Object> entry : group.entrySet()) {
            Object bean = entry.getValue();
            Method method = entry.getKey();
            Matcher matcher = null;
            String regx = null;
            if (type == 1){
                FriendMessageEvent annotation = method.getAnnotation(FriendMessageEvent.class);
                regx = annotation.matcher();
            }else{
                TemporaryMessageEvent annotation = method.getAnnotation(TemporaryMessageEvent.class);
                regx = annotation.matcher();
            }
            if (!regx.isEmpty()){
                matcher = matcher(text,regx);
                if (matcher != null) {
                    execInvoke(method,bean,body,matcher);
                    return true;
                }
                continue;
            }
            execInvoke(method,bean,body,matcher);
            return true;
        }
        return false;
    }



    private boolean execGroup(Map<Method, Object> group, MessageBody body, String text) throws InvocationTargetException, IllegalAccessException {
        Map<Method, Object> map = new HashMap<>(group.size());
        group.entrySet().stream().filter(e ->!e.getKey().getAnnotation(GroupMessageEvent.class).matcher().isEmpty())
                .forEach(e->map.put(e.getKey(),e.getValue()));
        if (!map.isEmpty() && invokeGroup(map, body, text)) {
            return true;
        }
        group.entrySet().stream().filter(e ->e.getKey().getAnnotation(GroupMessageEvent.class).matcher().isEmpty())
                .forEach(e->map.put(e.getKey(),e.getValue()));
        return invokeGroup(group,body,text);
    }
    private boolean invokeGroup(Map<Method, Object> group, MessageBody body, String text) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Method, Object> entry : group.entrySet()) {
            Object bean = entry.getValue();
            Method method = entry.getKey();
            GroupMessageEvent annotation = method.getAnnotation(GroupMessageEvent.class);
            if(handlerGroupMessage(body,text,annotation,bean,method)){
                return true;
            }
        }
        return false;
    }

    private boolean handlerGroupMessage(MessageBody body, String text, GroupMessageEvent messageEvent , Object bean,Method method) throws InvocationTargetException, IllegalAccessException {
        Matcher matcher = null;
        GroupMessage groupMessage = (GroupMessage) body;
        if (messageEvent.atMe() && !LwxUtil.isAtMe(groupMessage.getMessage(),groupMessage.getSelfId())) {
            return false;
        }
        else if (messageEvent.atAll() && !LwxUtil.isAtAll(groupMessage.getMessage())){
            return false;
        }
        matcher = matcher(text,messageEvent.matcher());

        if (!messageEvent.matcher().isEmpty()) {
            if (matcher != null) {
                execInvoke(method,bean,body,matcher);
                return true;
            }
        }else{
            execInvoke(method,bean,body,matcher);
            return true;
        }
        return false;
    }

    public Matcher matcher(String text,String regx){
        Pattern p = Pattern.compile(regx);
        Matcher matcher = p.matcher(text);
        if (!matcher.find()){
            return null;
        }
        return matcher;
    }

    private void execInvoke(Method method, Object bean, MessageBody body, Matcher matcher) throws InvocationTargetException, IllegalAccessException {
        method.invoke(bean, loadParams(method, body, matcher));
    }



    private boolean intercept(MessageBody body) {
        if (!intercept){
            return false;
        }
        Map<String, MessageIntercept> beans = context.getBeansOfType(MessageIntercept.class);
        if (!beans.isEmpty()) {
            //存在拦截器
            for (MessageIntercept bean : beans.values()){
                if (bean.inBefore(body)) {
                    return true;
                }
            }
        }
        return false;
    }
}
