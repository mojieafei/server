package com.example.server.service.grpc;

import com.example.server.exception.ConnectionException;
import com.example.server.gen.proto.*;
import com.example.server.service.grpc.Runnable.ConnectionCancelHandler;
import com.example.server.service.grpc.Runnable.ConnectionReadyHandler;
import com.example.server.utils.ConnectionUtils;
import io.grpc.stub.CallStreamObserver;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@GrpcService
public class ChatServiceGrpcImpl extends ChatServiceGrpc.ChatServiceImplBase {

    @Autowired
    private ConnectionUtils connectionUtils;


    @Override
    public void getConnection(ConnectionRequest request, StreamObserver<ConnectionReply> responseObserver) {
        try {
            if (request.getUserId() <= 0){
                throw new ConnectionException("Description Failed to create connection because the parameters are incorrect");
            }
            ConnectionReply.Builder builder = ConnectionReply.newBuilder();
            ServerCallStreamObserver<ConnectionReply> response =  (ServerCallStreamObserver)responseObserver;
            response.onNext(builder.setFormUserId(request.getUserId()).setText(Boolean.TRUE.toString()).build());
            response.setOnCancelHandler(new ConnectionCancelHandler(request.getUserId()));
            response.setOnReadyHandler(new ConnectionReadyHandler(request.getUserId(), response)); // 测试
            connectionUtils.putConnection(request.getUserId(), response);
            // 不释放链接
            // responseObserver.onCompleted();
        }
        catch (Exception e) {
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
            System.out.println("sendMessageToSomeOne error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }

    }


    @Override
    public void sendVersatileMessageToSomeone(VersatileMessageRequest request, StreamObserver<VersatileMessageReply> responseObserver) {
        try {
            VersatileMessageReply.Builder result = VersatileMessageReply.newBuilder();

            // 校验请求参数 && 发送功能性消息
            Code checkParam = checkParam(request);
            if(Code.SUCCESS.equals(checkParam) && doSendVersatileMessage(request)){
                result.setCode(checkParam).setResult(Boolean.TRUE);
            }else{
                result.setCode(checkParam).setResult(Boolean.FALSE);
            }

            responseObserver.onNext(result.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.out.println("sendMessageToSomeOne error ");
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    private boolean doSendMsg(SendMessageRequest request){
        // 推送消息
        ConnectionReply.Builder builder = build(request.getFormUserId(),request.getFormUserId() + "_" + request.getToUserId() + "_" + System.currentTimeMillis());
        CallStreamObserver<ConnectionReply> connection = connectionUtils.getConnection(request.getToUserId());
        // 接收方离线或者链接已经失效
        if(Objects.isNull(connection) || !connection.isReady()){
            // 离线消息逻辑
            System.out.println("处理离线逻辑");
            return Boolean.FALSE;
        }
        connection.onNext(builder.setText(request.getText()).build());
        // 消息持久化
        return Boolean.TRUE;
    }

    private boolean doSendVersatileMessage(VersatileMessageRequest request){
        // 推送消息
        ConnectionReply.Builder builder = build(request.getFormUserId(),request.getFormUserId() + "_" + request.getToUserId() + "_" + System.currentTimeMillis());
        CallStreamObserver<ConnectionReply> connection = connectionUtils.getConnection(request.getToUserId());
        // 接收方离线或者链接已经失效
        if(Objects.isNull(connection) || !connection.isReady()){
            // 离线消息逻辑
            System.out.println("处理离线逻辑");
            return Boolean.FALSE;
        }
        // 消息id
        connection.onNext(builder.setText("").setVersatileMessage(request.getVersatileMessage()).build());
        // 消息持久化
        return Boolean.TRUE;
    }

    private ConnectionReply.Builder build(Long fromUserId, String messageId){
        ConnectionReply.Builder builder = ConnectionReply.newBuilder();
        builder.setFormUserId(fromUserId).setMessageId(messageId);
        return builder;
    }


    private Code checkParam(SendMessageRequest request){
        return Code.SUCCESS;
    }

    private Code checkParam(VersatileMessageRequest request){
        return Code.SUCCESS;
    }



}
