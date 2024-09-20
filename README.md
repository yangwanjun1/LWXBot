# LWXBot
Lagrange机器人框架适配器，使用springboot进行封装
在使用之前，你已经安装部署好了Lagrange程序并成功运行
## 使用方法
1. 安装Lagrange<a href="https://github.com/LagrangeDev/Lagrange.Core/blob/master/Docker_zh.md#%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8">传送</a>
   并运行
2. 开放端口
3. 在release中下载最新版本jar包
4. 在本地springboot中引入坐标
```xml
<dependencies>
    <dependency>
        <groupId>lwx</groupId>
        <artifactId>com.lwx.lwx</artifactId>
        <systemPath>${project.basedir}/lib/LWX-0.0.1-SNAPSHOT.jar</systemPath>
        <version>1.0.1</version>
        <scope>system</scope>
    </dependency>
    <!--项目需要json解析-->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-json</artifactId>
        <version>5.8.28</version>
    </dependency>
    <!-- 项目需要websocket -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
</dependencies>
```
5. 在springboot中配置
```yaml
lwx:
  host: 127.0.0.1 # qq所在服务器
  ws-port: 8081     # ws端口 连接bot的端口
  client: true # true作为客户端，主动连接bot  false为作为服务端，等待bot连接 开启上上面两项会失效
  protocol: "http" # 可选http或https 发送消息的协议
  intercept: false  # 是否开启拦截器 
  port: 8082    # 发送消息给bot的端口 
  token: 1234567890  # 授权token 当作为服务端时，多个token用逗号隔开
  enabled-auth-connection: false # 是否开启鉴权
  server-path: /ws  #当作为server时的路径
  thread-poll-core: 2 # 线程池核心线程数
  thread-poll-max-size: 8 #线程池最大线程数
  thread-keep-alive-time: 30 #线程池空闲线程存活时间 秒
  thread-poll-block-size: 100 # 线程池阻塞队列大小
```
6.编写组件

```java
import lwx.bot.core.annotation.GroupMessageEvent;
import lwx.bot.core.annotation.LwxBot;
import lwx.bot.core.event.FriendMessage;
import lwx.bot.core.event.GroupMessage;
import lwx.bot.core.event.NoticeEvent;
import lwx.bot.core.event.TemporaryMessage;

@LwxBot
public class QQ {
    //监听群消息
    @GroupMessageEvent
    public void groupMessageEvent(GroupMessage message) {
//        发送消息
        message.sendText("你好");
//        回复
//        message.reply("你也好");
    }

    //监听指定群消息
    @GroupMessageEvent(listenerGroup = 123456789L)
    public void groupMessageEvent(GroupMessage message) {
//        发送图片
        message.sendImage("http://127.0.0.0/img/t.png");
//        发送图文消息
//        message.sendTextImage("http://127.0.0.0/img/t.png","哈哈哈");
    }

    //退群消息
    @LeaveGroupEvent
    public void groupMessageEvent(NoticeEvent message) {
    }


    //监听临时消息
    @TemporaryMessageEvent
    public void tempMessageEvent(TemporaryMessage message) {
    }

    //监听私聊消息
    @FriendMessageEvent
    public void friendMessage(FriendMessage message) {
    }
    
}

```
7.启动springboot项目

8.本人有点懒，因此功能不是很完善，文档也不是很全

[BanEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FBanEvent.java)
禁言注解

[FriendMessageEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FFriendMessageEvent.java)
私聊注解

[FriendRequestEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FFriendRequestEvent.java)
好友请求注解

[GroupMessageEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FGroupMessageEvent.java)
群消息注解

[GroupRecallEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FGroupRecallEvent.java)
群消息撤回注解

[GroupRequestEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FGroupRequestEvent.java)
群请求注解

[InviteGroupEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FInviteGroupEvent.java)
进群注解

[KickGroupEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FKickGroupEvent.java)
踢出群注解

[LeaveGroupEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FLeaveGroupEvent.java)
退群注解

[LiftBanEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FLiftBanEvent.java)
解除禁言注解

[LwxBot.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FLwxBot.java)
bot注解

[PokeEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FPokeEvent.java)
戳一戳注解

[SetGroupAdminEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FSetGroupAdminEvent.java)
设置管理注解

[TemporaryMessageEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FTemporaryMessageEvent.java)
临时消息注解

[UnsetGroupAdminEvent.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fannotation%2FUnsetGroupAdminEvent.java)
取消管理注解

[ManagerBot.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2Fbody%2FManagerBot.java)
机器人容器

[Bot.java](src%2Fmain%2Fjava%2Flwx%2Fbot%2Fcore%2FBot.java)
机器人实体
