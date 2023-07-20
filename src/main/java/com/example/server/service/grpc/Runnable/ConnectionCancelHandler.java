package com.example.server.service.grpc.Runnable;

import com.example.server.utils.ApplicationContextUtil;
import com.example.server.utils.ConnectionUtils;

public class ConnectionCancelHandler implements Runnable {

    private long userId;

    public ConnectionCancelHandler(long userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        // We can do something here. 如果链接取消，不管因为什么、用户主动、还是链接超时、意外报错取消、都可以在这里移除掉
        ConnectionUtils connectionUtils = (ConnectionUtils) ApplicationContextUtil.getBean(ConnectionUtils.class);
        connectionUtils.remove(userId);
        System.out.println("链接取消前执行");
    }
}
