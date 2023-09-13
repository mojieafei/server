package com.example.server;

import io.grpc.internal.JsonUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyThread myThread = new MyThread();
        FutureTask<Object> objectFutureTask = new FutureTask<Object>(myThread);
        new Thread(objectFutureTask).start();
        System.out.println(objectFutureTask.get());

    }

    static class MyThread implements Callable{

        @Override
        public Object call() throws Exception {
            System.out.println(1);
            return "你好";
        }
    }
}
