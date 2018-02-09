package com.lightconf.common.util;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuhf
 * @date 2018/02/09
 */
public class NettyChannelMap {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<String, SocketChannel>();

    public static void add(String clientId, SocketChannel socketChannel) {
        map.put(clientId, socketChannel);
    }

    public static Channel get(String clientId) {
        return map.get(clientId);
    }

    public static String remove(SocketChannel socketChannel) {
        String key = null;
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                key = (String) entry.getKey();
                map.remove(key);
            }
        }
        return key;
    }

    /**
     * 根据channel获取connectorName.
     * @param socketChannel socketChannel.
     * @return connectorName.
     */
    public static String getClientId(SocketChannel socketChannel) {
        String key = null;
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                key = (String) entry.getKey();
            }
        }
        return key;
    }

}
