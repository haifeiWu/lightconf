package com.lightconf.admin.web.dal.cache;


import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;

/**
 * Created by qiuliang on 16/3/7.
 */
public final class CacheClientUtils {
    public static String formatKey(String key, String area, String preFix) {
        return String.format("%s_%s_%s", area, preFix, key);
    }

    public static ArrayList<CacheNode> getNodes(String config) throws Exception {
        if (StringUtils.isNullOrEmpty(config)) {
            throw new Exception("config can not be null");
        }
        String[] hosts = config.split(",");
        ArrayList<CacheNode> nodes = new ArrayList<>();

        for (String h : hosts) {
            String[] hp = h.split(":");
            CacheNode cacheNode = new CacheNode();
            cacheNode.setHost(hp[0]);
            cacheNode.setPort(Integer.parseInt(hp[1]));
            nodes.add(cacheNode);
        }

        return nodes;
    }
}
