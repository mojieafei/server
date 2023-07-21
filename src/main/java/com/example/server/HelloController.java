package com.example.server;

import com.alibaba.fastjson.JSON;
import com.example.server.common.RedisKey;
import com.example.server.utils.ConnectionUtils;
import com.example.server.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@RestController
@Slf4j
public class HelloController  extends JedisPubSub {

    @Autowired
    private ConnectionUtils connectionUtils;

    @Autowired
    private JedisPoolUtils jedisPoolUtils;

    @GetMapping("hello")
    public String hello(){
    //    HashMap<String, String> urlParam = new HashMap<>();
    //    HashMap<String, String> text = new HashMap<>();
    //        text.put("text","用户" + request.getFormUserId() + "向" + "用户" + request.getToUserId() + "成功发送了消息，消息为：" + request.getText());
    //        urlParam.put("body",JSON.toJSONString(text));
    //        HttpUtil.sendPost("https://www.feishu.cn/flow/api/trigger-webhook/e4d20740bd1f84f2657b45b5e1679837", urlParam, "UTF-8");
        return "hello";
    }



    @GetMapping("testJedis")
    public String testJedis(){
        Jedis jedis = jedisPoolUtils.getJedis();
        jedis.publish(RedisKey.MESSAGE_PUSH_CHANNEL,"你是谁？");
        jedis.close();
        return "success";
    }

    @GetMapping("testJedisLink")
    public String testJedisLink(){
        for (int i =0;i<=999;i++){
            Jedis jedis = jedisPoolUtils.getJedis();
            jedis.set("" + i, "qunimeide " + i);
        }
        log.info("执行完毕");
        return "success";
    }

    @GetMapping("testJedis2")
    public String testJedis2(){
        Jedis jedis = jedisPoolUtils.getJedis();
        jedis.subscribe(this, RedisKey.MESSAGE_PUSH_CHANNEL);
        jedis.close();
        return "success";
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("订阅了  然后呢？" + channel + "——" + message);
    }

    @GetMapping("lookup")
    public String lookup(){

        return JSON.toJSONString(connectionUtils.getConnections());
    }

}
