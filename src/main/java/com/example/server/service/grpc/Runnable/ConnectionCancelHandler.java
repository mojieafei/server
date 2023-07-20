package com.example.server.service.grpc.Runnable;

public class ConnectionCancelHandler implements Runnable {
    @Override
    public void run() {
        // We can do something here.
        System.out.println("链接取消前执行");
    }
}
