package com.tingyouqu.server.service.grpc;

import com.tingyouqu.server.common.RedisKey;
import com.tingyouqu.server.gen.proto.ConnectionReply;
import com.tingyouqu.server.utils.ConnectionUtils;
import com.tingyouqu.server.utils.JedisPoolUtils;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.CallStreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.Objects;


@Component
@Slf4j
public class chatServicePubSub extends JedisPubSub {


    private static volatile Thread subscriber;

    @Autowired
    private JedisPoolUtils jedisPoolUtils;

    @Autowired
    private ConnectionUtils connectionUtils;

    @Autowired
    private ChatServiceGrpcImpl chatServiceGrpc;

    @SneakyThrows
    @Override
    public void onMessage(String channel, String message) {
        ConnectionReply.Builder builder = ConnectionReply.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(message, builder);
        log.info("订阅 收到了消息" + channel + "——" + builder.getMessageId());
        // 暂时只处理在线消息
        CallStreamObserver<ConnectionReply> connection = connectionUtils.getConnection(builder.getToUserId());
        // 接收方离线或者链接已经失效
        if(Objects.isNull(connection) || !connection.isReady()){
            // 离线消息逻辑
            System.out.println("处理离线逻辑");
        }
        connection.onNext(builder.build());
    }

    //    @EventListener(ContextRefreshedEvent.class)  //监听事件启动也可以，不过还是用初始化前就够了
    @PostConstruct           //初始化前init
    public void init() {
        try {
            subscriber = new Thread(() -> {
                try {
                    log.info("chatServicePubSub subscriber starting");
                    jedisPoolUtils.getJedis().subscribe(this, RedisKey.MESSAGE_PUSH_CHANNEL);
                    log.error("chatServicePubSub subscriber shutdown");
                } catch (Exception e) {
                    log.error("chatServicePubSub init subscriber exception", e);
                }
            });
            subscriber.setName("chatService#subscriber");
            subscriber.start();
        } catch (Exception e) {
                log.error("chatService init subscriber error", e);
        }
    }

}
