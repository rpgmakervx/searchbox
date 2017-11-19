package org.code4j.searchbox.conf;

import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author xingtianyu(code4j) Created on 2017-11-18.
 */
public class AppConfig {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AppConfig.class.getName());
    private static Properties prop = new Properties();

    public static void init(String configPath) {
        try (InputStream in = new FileInputStream(configPath)) {
            prop.load(in);
        } catch (IOException e) {
            logger.error("load appConfig配置文件 {} error.",configPath, e);
        }
    }

    /**
     * 根据name 获取配置信息
     *
     * @param name
     * @return
     */
    public static String getProperty(String name) {
        if (prop == null || !prop.containsKey(name)) {
            logger.warn("无 {} 配置信息...",name);
            return null;
        }
        String result = prop.getProperty(name);
        if (result != null) {
            result = result.trim();
        }
        return result;
    }
}
