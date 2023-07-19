package com.example.server.service.grpc;

import com.alibaba.fastjson.JSON;
import com.example.server.gen.proto.*;
import com.example.server.utils.ConnectionUtils;
import com.example.server.utils.HttpUtil;
import io.grpc.internal.JsonUtil;
import io.grpc.stub.CallStreamObserver;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@GrpcService
public class ChatServiceGrpcImpl extends ChatServiceGrpc.ChatServiceImplBase {

    @Autowired
    private ConnectionUtils connectionUtils;



    @Override
    public void getConnection(ConnectionRequest request, StreamObserver<ConnectionReply> responseObserver) {
        try {
            ConnectionReply.Builder builder = ConnectionReply.newBuilder();
            CallStreamObserver<ConnectionReply> response =  (CallStreamObserver)responseObserver;
            response.onNext(builder.setFormUserId(request.getUserId()).setText(Boolean.TRUE.toString()).build());
            connectionUtils.putConnection(request.getUserId(), response);
            // 不释放链接
            // responseObserver.onCompleted();
        } catch (Exception e) {
//            log.error("sendMessageToSomeone error", e);  记录日志
            System.out.println("getConnection error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void giveUpConnection(ConnectionRequest request, StreamObserver<GiveUpConnectionReply> responseObserver) {
        try {

            GiveUpConnectionReply.Builder result = GiveUpConnectionReply.newBuilder();
            result.setResult(Boolean.TRUE);
            StreamObserver<ConnectionReply> connection = connectionUtils.getConnection(request.getUserId());
            connection.onCompleted();
            responseObserver.onNext(result.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
//            log.error("sendMessageToSomeone error", e);  记录日志
            System.out.println("giveUpConnection error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void sendMessageToSomeone(SendMessageRequest request, StreamObserver<SendMessageReply> responseObserver) {
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
//            log.error("sendMessageToSomeone error", e);  记录日志
            System.out.println("sendMessageToSomeOne error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }

    }



    private boolean doSendMsg(SendMessageRequest request){
//        System.out.println("发送了消息");
        // 推送消息
        ConnectionReply.Builder builder = ConnectionReply.newBuilder();
        CallStreamObserver<ConnectionReply> connection = connectionUtils.getConnection(request.getToUserId());
        // 接收方离线或者链接已经失效
        if(Objects.isNull(connection) || !connection.isReady()){
            // 离线消息逻辑
            System.out.println("处理离线逻辑");
            return Boolean.FALSE;
        }
        connection.onNext(builder.setFormUserId(request.getFormUserId()).setText(request.getText()).build());
        return Boolean.TRUE;
    }


//    HashMap<String, String> urlParam = new HashMap<>();
//    HashMap<String, String> text = new HashMap<>();
//        text.put("text","用户" + request.getFormUserId() + "向" + "用户" + request.getToUserId() + "成功发送了消息，消息为：" + request.getText());
//        urlParam.put("body",JSON.toJSONString(text));
//        HttpUtil.sendPost("https://www.feishu.cn/flow/api/trigger-webhook/e4d20740bd1f84f2657b45b5e1679837", urlParam, "UTF-8");
    private Code checkParam(SendMessageRequest request){
        return Code.SUCCESS;
    }
}
