package com.example.server.service.grpc;

import com.example.server.gen.proto.ChatServiceGrpc;
import com.example.server.gen.proto.Code;
import com.example.server.gen.proto.SendMessageReply;
import com.example.server.gen.proto.SendMessageRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceGrpcImpl extends ChatServiceGrpc.ChatServiceImplBase {




    public void sendMessageToSomeOne(SendMessageRequest request, StreamObserver<SendMessageReply> responseObserver) {
        try {
            SendMessageReply.Builder result = SendMessageReply.newBuilder();

            // 校验请求参数 && 模拟发送消息服务
            Code checkParam = checkParam(request);
            if(Code.SUCCESS.equals(checkParam) && doSendMsg(request)){
                result.setCode(checkParam).setResult(Boolean.TRUE);
            }else{
                result.setCode(checkParam).setResult(Boolean.FALSE);
            }



            responseObserver.onNext(result.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
//            log.error("addTicketToCasinosList error", e);  记录日志
            System.out.println("sendMessageToSomeOne error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }

    }

    private boolean doSendMsg(SendMessageRequest request){
        System.out.println("发送了消息");
        return Boolean.TRUE;
    }

    private Code checkParam(SendMessageRequest request){
        return Code.SUCCESS;
    }
}
