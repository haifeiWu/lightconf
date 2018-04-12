package com.lightconf.core.listener;

/**
 * light conf listener
 *
 * @author wuhaifei
 */
public interface LightConfListener {

    /**
     * invoke when light conf change
     *
     * @param key
     */
    public void onChange(String key, String value) throws Exception;

}
