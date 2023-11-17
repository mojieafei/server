package com.tingyouqu.server.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;

@Component
@Slf4j
public class JedisPoolUtils {

   @Autowired
   private JedisPool jedisPool;


   public Jedis getJedis(){
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(Jedis.class);
      enhancer.setCallback(new JedisPoolUtils.JRedis());
      return (Jedis)enhancer.create();
   }





   // CGLIB Enhancer代理= 自动关闭 免去了手动close
   private static final class JRedis implements MethodInterceptor {


      private JRedis() {
      }

      public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
         if ("pipelined".equals(method.getName())) {
            throw new RuntimeException("pipelined cannot auto close");
         } else if ("close".equals(method.getName())) {
            throw new RuntimeException("close illegal");
         } else {
            log.debug("Call Jedis Method: " + method.getName());
            JedisPool jedisPool = (JedisPool) ApplicationContextUtil.getBean(JedisPool.class);
            Jedis jedisObject = null;
            Object result = null;

            try {
               jedisObject = jedisPool.getResource();
               result = methodProxy.invoke(jedisObject, args);
            } catch (Exception var12) {
               log.error("Call Jedis Method: " + method + " exception " + var12.getMessage(), var12);
            } finally {
               if (jedisObject != null) {
                  jedisObject.close();
               }

            }

            return result;
         }
      }
   }



}
