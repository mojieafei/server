package com.example.server.utils;

import com.alibaba.fastjson.JSON;
import com.example.server.exception.ConnectionException;
import com.example.server.gen.proto.ConnectionReply;
import io.grpc.stub.ServerCallStreamObserver;
import org.springframework.stereotype.Component;

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

    private ConcurrentHashMap<Long, ServerCallStreamObserver<ConnectionReply>> connections = new ConcurrentHashMap<>();

    public void putConnection(Long userId, ServerCallStreamObserver<ConnectionReply> responseObserver) throws ConnectionException {
        if (userId <= 0 || Objects.isNull(responseObserver)){
            throw new ConnectionException("Description Failed to create connection because the parameters are incorrect");
        }
        ServerCallStreamObserver<ConnectionReply> old = connections.get(userId);
        if(Objects.isNull(old) || !old.isReady()){
            connections.put(userId,responseObserver);
        }
//        System.out.println(JSON.toJSONString(connections));
    }

    public ServerCallStreamObserver<ConnectionReply>  getConnection(Long userId){
        ServerCallStreamObserver<ConnectionReply> connectionReplyStreamObserver = connections.get(userId);
        if(Objects.isNull(connectionReplyStreamObserver)){
            return null;
        }
        return connectionReplyStreamObserver;
    }

    public ServerCallStreamObserver<ConnectionReply> getConnectionAndRemove(Long userId){
        ServerCallStreamObserver<ConnectionReply> connectionReplyStreamObserver = connections.get(userId);
        connections.remove(userId);
        if(Objects.isNull(connectionReplyStreamObserver)){
            return null;
        }
        return connectionReplyStreamObserver;
    }

    public void remove(Long userId){
        connections.remove(userId);
    }

    public boolean contains(Long userId){
        return connections.contains(userId);
    }

    public ConcurrentHashMap<Long, ServerCallStreamObserver<ConnectionReply>> getConnections(){
        return connections;
    }


}
