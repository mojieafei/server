package com.example.server.service.grpc;

import com.example.server.gen.proto.HelloReply;
import com.example.server.gen.proto.HelloRequest;
import com.example.server.gen.proto.TestGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Scanner;

@GrpcService
public class TestGrpcService extends TestGrpc.TestImplBase{
    @Override
    public StreamObserver<HelloRequest> chatHello(StreamObserver<HelloReply> responseObserver) {
        StreamObserver<HelloRequest> result = new StreamObserver
                <HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                System.out.println("对方像向你说：" + helloRequest.getText());

                for (;;){
                    Scanner str=new Scanner(System.in);
                    String next = str.next();
                    if("baibai".equals(next)){
                        break;
                    }
                    HelloReply build1 = HelloReply.newBuilder().setText(next).build();
                    responseObserver.onNext(build1);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return result;
    }
}
