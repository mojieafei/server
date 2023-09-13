package com.example.server.service.grpc.Runnable;

import com.example.server.gen.proto.ConnectionReply;
import io.grpc.stub.ServerCallStreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionReadyHandler implements Runnable {


    private long userId;
    private ServerCallStreamObserver<ConnectionReply> response;


    public ConnectionReadyHandler(long userId, ServerCallStreamObserver<ConnectionReply> response) {
        this.userId = userId;
        this.response = response;
    }

    @Override
    public void run() {
        // We can do something here.
        log.info(userId + " is ready!");
    }
}
