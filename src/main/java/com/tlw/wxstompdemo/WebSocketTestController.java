package com.tlw.wxstompdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * webSocket测试
 *
 * @author lihy
 * @version 2018/6/15
 */
@Controller
@EnableScheduling
public class WebSocketTestController {

    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 定时推送消息
     *
     * @author lihy
     */
    @Scheduled(fixedRate = 1000)
    public void callback() {
        // 发现消息
        System.out.println("Callback...");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpMessageSendingOperations.convertAndSend("/topic/greetings", "广播消息" + df.format(new Date()));
    }

    /**
     * 发送给单一客户端
     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
     *
     * @return java.lang.String
     * @author lihy
     */
    @Scheduled(fixedRate = 1000)
    public void sendToUser() {
        System.out.println("Send to user: ...");
        String openid = "60c570f7a4343d9645c6ba44be2baad2";
        simpMessageSendingOperations.convertAndSendToUser(openid, "/message", "你好" + openid);
    }
}
