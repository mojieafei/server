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

        return "hello";
    }

    @GetMapping("lookup")
    public String lookup(){

        return JSON.toJSONString(connectionUtils.getConnections());
    }

}
