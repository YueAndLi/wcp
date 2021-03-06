package com.farm.doc.server.commons;

import org.apache.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FastDfsConfig {
    private static final String BUNDLE_NAME = "fastdfs"; //$NON-NLS-1$
    private static final Logger log = Logger.getLogger(DocumentConfig.class);
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private FastDfsConfig() {
    }

    /**
     * 从properties文件中获得配置值
     *
     * @param key 配置文件的key
     * @return
     */
    public static String getString(String key) {
        try {
            String messager = RESOURCE_BUNDLE.getString(key);
            return messager;
        } catch (MissingResourceException e) {
            String messager = "不能在配置文件" + BUNDLE_NAME + "中发现参数：" + '!' + key
                    + '!';
            log.error(messager);
            throw new RuntimeException(messager);
        }
    }
}
