package com.lightconf.core;

import com.lightconf.core.core.XxlConfZkClient;
import org.apache.zookeeper.KeeperException;

public class XxlConfZkClientTest {

    public static void main(String[] args) throws InterruptedException, KeeperException {
        XxlConfZkClient.setPathDataByKey("key02", "666");
        System.out.println(XxlConfZkClient.getPathDataByKey("key02"));
        XxlConfZkClient.deletePathByKey("key02");

    }

}
