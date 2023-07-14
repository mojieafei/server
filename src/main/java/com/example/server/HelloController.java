package com.example.server;

import com.example.server.gen.proto.HelloReply;
import com.example.server.gen.proto.HelloRequest;
import com.example.server.gen.proto.TestGrpc;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
public class HelloController {

    @GrpcClient("server-grpc")
    private TestGrpc.TestStub testStub;

    @GetMapping("hello")
    public String hello(){

        StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {            // 用stringBuilder保存所有来自服务端的响应

            @Override
            public void onNext(HelloReply helloReply) {
                System.out.println("用户B：");
                System.out.println(helloReply.getText());
            }

            @Override
            public void onError(Throwable t) {
            }

            /**
             * 服务端确认响应完成后，这里的onCompleted方法会被调用
             */
            @Override
            public void onCompleted() {
                System.out.println("系统消息：聊天聊不下去了");
            }
        };

        StreamObserver<HelloRequest> helloRequestStreamObserver = testStub.chatHello(responseObserver);
        HelloRequest build = HelloRequest.newBuilder().setText("我想找你聊天啊").build();
        helloRequestStreamObserver.onNext(build);
        helloRequestStreamObserver.onCompleted();

        return "hello";
    }
//    /**
//     * 批量减库存
//     * @param count
//     * @return
//     */
//    public String batchDeduct(int count) {
//        CountDownLatch countDownLatch = new CountDownLatch(1);        // responseObserver的onNext和onCompleted会在另一个线程中被执行，
//        // ExtendResponseObserver继承自StreamObserver
//
//        // 远程调用，此时数据还没有给到服务端
//        StreamObserver<ProductOrder> requestObserver = testBlockingStub.batchDeduct(responseObserver);        for(int i=0; i<count; i++) {
//            // 每次执行onNext都会发送一笔数据到服务端，
//            // 服务端的onNext方法都会被执行一次
//            requestObserver.onNext(build(101 + i, 1 + i));
//        }        // 客户端告诉服务端：数据已经发完了
//        requestObserver.onCompleted();        try {
//            // 开始等待，如果服务端处理完成，那么responseObserver的onCompleted方法会在另一个线程被执行，
//            // 那里会执行countDownLatch的countDown方法，一但countDown被执行，下面的await就执行完毕了，
//            // await的超时时间设置为2秒
//            countDownLatch.await(2, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            log.error("countDownLatch await error", e);
//        }        log.info("service finish");
//        // 服务端返回的内容被放置在requestObserver中，从getExtra方法可以取得
//        return responseObserver.getExtra();
//    }

}
