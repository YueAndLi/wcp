package com.farm.parameter.service.impl;

import com.farm.parameter.controller.ParameterController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

@Service
public class PropertiesFileService {
    private static Set<ResourceBundle> constant = new HashSet<ResourceBundle>();
    private static final Logger log = Logger
            .getLogger(ParameterController.class);

    /**
     * 注册配置文件
     *
     * @param fileName "config"=config.properties
     */
    public static boolean registConstant(String fileName) {
        log.info("注册配置文件" + fileName + ".properties");
        return constant.add(ResourceBundle.getBundle(fileName));
    }

    public static List<Entry<String, String>> getEntrys() {
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
        for (ResourceBundle node : constant) {
            Enumeration<String> enums = node.getKeys();
            while (enums.hasMoreElements()) {
                String key = enums.nextElement();
                Entry<String, String> entry = new SimpleEntry<String, String>(
                        key, node.getString(key));
                list.add(entry);
            }
        }
        return list;
    }

    public static String getValue(String key) {
        for (ResourceBundle node : constant) {
            String value = getString(key, node);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String getString(String key, ResourceBundle bundle) {
        try {
            String messager = bundle.getString(key);
            return messager;
        } catch (MissingResourceException e) {
            return null;
        }
    }
}
