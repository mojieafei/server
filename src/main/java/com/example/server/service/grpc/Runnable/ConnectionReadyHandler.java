package com.example.server.service.grpc.Runnable;

import com.example.server.gen.proto.ConnectionReply;
import io.grpc.stub.ServerCallStreamObserver;

public class ConnectionReadyHandler implements Runnable {

    private long userId;
    private ServerCallStreamObserver<ConnectionReply> response;

    public ConnectionReadyHandler(long userId, ServerCallStreamObserver<ConnectionReply> response) {
        userId = userId;
        response = response;
    }

    @Override
    public void run() {
        // We can do something here.
        System.out.println(userId + " is ready!");
    }
}
