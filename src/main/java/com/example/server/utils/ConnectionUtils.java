package com.example.server.utils;

import com.alibaba.fastjson.JSON;
import com.example.server.gen.proto.ConnectionReply;
import io.grpc.stub.CallStreamObserver;
import io.grpc.stub.StreamObserver;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户链接管理工具
 */

@Component
public class ConnectionUtils {

    /**
     * 初版先放在内存里
     *  key：用户id，value：
     */

    private ConcurrentHashMap<Long, CallStreamObserver<ConnectionReply>> connections = new ConcurrentHashMap<>();

    public void putConnection(@Positive(message = "id非法")Long userId, @NotNull(message = "链接无效")CallStreamObserver<ConnectionReply> responseObserver){
        connections.put(userId,responseObserver);
        System.out.println(JSON.toJSONString(connections));
    }

    public CallStreamObserver<ConnectionReply>  getConnection(Long userId){
        CallStreamObserver<ConnectionReply> connectionReplyStreamObserver = connections.get(userId);
        if(Objects.isNull(connectionReplyStreamObserver)){
            return null;
        }
        return connectionReplyStreamObserver;
    }

    public CallStreamObserver<ConnectionReply> getConnectionAndRemove(Long userId){
        CallStreamObserver<ConnectionReply> connectionReplyStreamObserver = connections.get(userId);
        connections.remove(userId);
        if(Objects.isNull(connectionReplyStreamObserver)){
            return null;
        }
        return connectionReplyStreamObserver;
    }

    public ConcurrentHashMap<Long, CallStreamObserver<ConnectionReply>> getConnections(){
        return connections;
    }


}
