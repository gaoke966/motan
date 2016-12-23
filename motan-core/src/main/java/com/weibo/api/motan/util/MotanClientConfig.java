package com.weibo.api.motan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author kegao@creditease.cn
 * @version V1.0
 * @Description:
 * @date 2016/12/23
 * @Company: creditease.cn
 */
public class MotanClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(MotanClientConfig.class);
    private  Properties specialProperties = new Properties();
    public MotanClientConfig(String specialConfig) {
        try {
            specialProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(specialConfig));
            logger.info("read specialConfig--->"+specialConfig+" success");
        } catch (FileNotFoundException e) {
            logger.info("read specialConfig--->"+specialConfig+" fail",e);
        } catch (Exception e) {
            logger.info("load specialConfig--->"+specialConfig+" fail",e);
        }
    }

    private static Properties props = new Properties();
    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("motanclient.properties"));
            logger.info("read specialConfig--->motanclient.properties success");
        } catch (FileNotFoundException e) {
            logger.info("read specialConfig--->motanclient.properties fail",e);
        } catch (Exception e) {
            logger.info("load specialConfig--->motanclient.properties fail",e);
        }
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static Properties getProperties() {
        return props;
    }

    public String getSpecialValue(String key) {
        return specialProperties.getProperty(key);
    }

    public Properties getSpecialProperties() {
        return specialProperties;
    }

}
