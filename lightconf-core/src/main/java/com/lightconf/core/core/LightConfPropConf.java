package com.lightconf.core.core;

import com.lightconf.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuhf
 * @date 2018/02/11
 */
public class LightConfPropConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightConfPropConf.class);

    private static final ConcurrentHashMap<String, String> propConf = new ConcurrentHashMap<>();

    static {
        // default prop
        Properties prop = loadProp(Environment.LIGHT_CONF_PROP);
        if (prop.stringPropertyNames() != null && prop.stringPropertyNames().size() > 0) {

            // prop file
            String propFileLocation = prop.getProperty(Environment.LIGHT_PROP_FILE_LOCATION);
            if (propFileLocation != null && propFileLocation.trim().length() > 0) {
                prop = loadProp(propFileLocation);
            }

            // 加载配置到内存中
            for (String key : prop.stringPropertyNames()) {
                propConf.put(key, prop.getProperty(key));
            }
        }

        LOGGER.info(">>>>>>>>>> light-conf, LightConfPropConf init success.");
    }

    private static Properties loadProp(String propertyFileName) {
        Properties prop = new Properties();
        InputStream in = null;
        try {

            // load file location, disk or resource
            URL url = null;
            if (propertyFileName.startsWith("file:")) {
                url = new File(propertyFileName.substring("file:".length())).toURI().toURL();
            } else {
                ClassLoader loder = Thread.currentThread().getContextClassLoader();
                url = loder.getResource(propertyFileName);
            }

            if (url != null) {
                in = new FileInputStream(url.getPath());
                if (in != null) {
                    prop.load(in);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return prop;
    }

    /**
     * get conf from local prop
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return propConf.get(key);
    }
}
