package com.example.server;

import com.alibaba.fastjson.JSON;
import com.example.server.utils.ConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private ConnectionUtils connectionUtils;

    @GetMapping("hello")
    public String hello(){
    //    HashMap<String, String> urlParam = new HashMap<>();
    //    HashMap<String, String> text = new HashMap<>();
    //        text.put("text","用户" + request.getFormUserId() + "向" + "用户" + request.getToUserId() + "成功发送了消息，消息为：" + request.getText());
    //        urlParam.put("body",JSON.toJSONString(text));
    //        HttpUtil.sendPost("https://www.feishu.cn/flow/api/trigger-webhook/e4d20740bd1f84f2657b45b5e1679837", urlParam, "UTF-8");
        return "hello";
    }

    @GetMapping("lookup")
    public String lookup(){

        return JSON.toJSONString(connectionUtils.getConnections());
    }

}
