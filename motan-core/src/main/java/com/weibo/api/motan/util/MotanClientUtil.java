package com.weibo.api.motan.util;

import com.alibaba.fastjson.JSONObject;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by kegao on 2016/12/20.
 */
public class MotanClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(MotanClientUtil.class);
    /**
     * 获取motan服务接口的代理实例
     * @param tClass：XxxApi.class
     * @param group:server端对应组
     * @param <T>
     * @return T
     */
    public static<T> T getMotanServiceReferer(Class<T> tClass, String group) {
        return MotanClientUtil.getMotanServiceReferer(tClass,group,null);
    }

    /**
     * 获取motan服务接口的代理实例
     * @param tClass：XxxApi.class
     * @param group:server端对应组
     * @param directUrl:指定调用机器或者调用本地服务需添加此配置
     * @param <T>
     * @return T
     */
    public static<T> T getMotanServiceReferer(Class<T> tClass, String group, String directUrl) {
        return MotanClientUtil.getMotanServiceReferer(tClass,group,directUrl,null);
    }

    /**
     * 获取motan服务接口的代理实例
     * @param tClass：XxxApi.class
     * @param group:server端对应组
     * @param directUrl:指定调用机器或者调用本地服务需添加此配置
     * @param specialConfig:指定外部自定义文件路径读取配置属性
     * @param <T>
     * @return T
     */
    public static<T> T getMotanServiceReferer(Class<T> tClass, String group, String directUrl, String specialConfig) {

        //ServiceReferer默认配置
        String rVersion = "1.0";
        Integer rRequestTimeout = 3000;
        Integer rRetries = 2;
        String rAccessLog = "false";
        String rCheck = "true";

        //RegistryConfig默认配置
        String zRegProtocol = "zookeeper";
        String zAddress = "10.100.138.170:2181";
        String zUsername = "";
        String zPassword = "";
        Integer zConnectTimeout = 10000;

        //ProtocolConfig默认配置
        String pId = "motan";
        String pName = "motan";
        String pHaStrategy = "failover";
        String pLoadbalance = "roundrobin";
        Integer pRequestTimeout = 2000;
        Integer pMaxClientConnection = 500;
        Integer pMinClientConnection = 20   ;

        if(specialConfig == null || "".equals(specialConfig)) {
            if (MotanClientConfig.getProperties().size() > 0) {
                if (MotanClientConfig.getValue("rVersion") != null) rVersion = MotanClientConfig.getValue("rVersion");
                if (MotanClientConfig.getValue("rRequestTimeout") != null) rRequestTimeout = new Integer(MotanClientConfig.getValue("rRequestTimeout"));
                if (MotanClientConfig.getValue("rRetries") != null) rRetries = new Integer(MotanClientConfig.getValue("rRetries"));
                if (MotanClientConfig.getValue("rAccessLog") != null) rAccessLog = MotanClientConfig.getValue("rAccessLog");
                if (MotanClientConfig.getValue("rCheck") != null) rCheck = MotanClientConfig.getValue("rCheck");
                if (MotanClientConfig.getValue("zRegProtocol") != null) zRegProtocol = MotanClientConfig.getValue("zRegProtocol");
                if (MotanClientConfig.getValue("zAddress") != null) zAddress = MotanClientConfig.getValue("zAddress");
                if (MotanClientConfig.getValue("zUsername") != null) zUsername = MotanClientConfig.getValue("zUsername");
                if (MotanClientConfig.getValue("zPassword") != null) zPassword = MotanClientConfig.getValue("zPassword");
                if (MotanClientConfig.getValue("zConnectTimeout") != null) zConnectTimeout = new Integer(MotanClientConfig.getValue("zConnectTimeout"));
                if (MotanClientConfig.getValue("pId") != null) pId = MotanClientConfig.getValue("pId");
                if (MotanClientConfig.getValue("pName") != null) pName = MotanClientConfig.getValue("pName");
                if (MotanClientConfig.getValue("pHaStrategy") != null) pHaStrategy = MotanClientConfig.getValue("pHaStrategy");
                if (MotanClientConfig.getValue("pLoadbalance") != null) pLoadbalance = MotanClientConfig.getValue("pLoadbalance");
                if (MotanClientConfig.getValue("pRequestTimeout") != null) pRequestTimeout = new Integer(MotanClientConfig.getValue("pRequestTimeout"));
                if (MotanClientConfig.getValue("pMaxClientConnection") != null) pMaxClientConnection = new Integer(MotanClientConfig.getValue("pMaxClientConnection"));
                if (MotanClientConfig.getValue("pMinClientConnection") != null) pMinClientConnection = new Integer(MotanClientConfig.getValue("pMinClientConnection"));
            }else{
                logger.info("读取默认属性文件--->失败,将使用默认值！- 原因：文件名motanclient.properties错误或者文件不存在！");
            }
        }else{
            MotanClientConfig motanClientConfig = new MotanClientConfig(specialConfig);
            if(motanClientConfig.getSpecialProperties().size() > 0){
                if (motanClientConfig.getValue("rVersion") != null) rVersion = motanClientConfig.getValue("rVersion");
                if (motanClientConfig.getValue("rRequestTimeout") != null) rRequestTimeout = new Integer(motanClientConfig.getValue("rRequestTimeout"));
                if (motanClientConfig.getValue("rRetries") != null) rRetries = new Integer(motanClientConfig.getValue("rRetries"));
                if (motanClientConfig.getValue("rAccessLog") != null) rAccessLog = motanClientConfig.getValue("rAccessLog");
                if (motanClientConfig.getValue("rCheck") != null) rCheck = motanClientConfig.getValue("rCheck");
                if (motanClientConfig.getValue("zRegProtocol") != null) zRegProtocol = motanClientConfig.getValue("zRegProtocol");
                if (motanClientConfig.getValue("zAddress") != null) zAddress = motanClientConfig.getValue("zAddress");
                if (motanClientConfig.getValue("zUsername") != null) zUsername = motanClientConfig.getValue("zUsername");
                if (motanClientConfig.getValue("zPassword") != null) zPassword = motanClientConfig.getValue("zPassword");
                if (motanClientConfig.getValue("zConnectTimeout") != null) zConnectTimeout = new Integer(motanClientConfig.getValue("zConnectTimeout"));
                if (motanClientConfig.getValue("pId") != null) pId = motanClientConfig.getValue("pId");
                if (motanClientConfig.getValue("pName") != null) pName = motanClientConfig.getValue("pName");
                if (motanClientConfig.getValue("pHaStrategy") != null) pHaStrategy = motanClientConfig.getValue("pHaStrategy");
                if (motanClientConfig.getValue("pLoadbalance") != null) pLoadbalance = motanClientConfig.getValue("pLoadbalance");
                if (motanClientConfig.getValue("pRequestTimeout") != null) pRequestTimeout = new Integer(motanClientConfig.getValue("pRequestTimeout"));
                if (motanClientConfig.getValue("pMaxClientConnection") != null) pMaxClientConnection = new Integer(motanClientConfig.getValue("pMaxClientConnection"));
                if (motanClientConfig.getValue("pMinClientConnection") != null) pMinClientConnection = new Integer(motanClientConfig.getValue("pMinClientConnection"));

            }else{
                logger.info("读取自定义属性文件--->失败,将使用默认值！- 原因：文件名"+specialConfig+"错误或者文件不存在！");
            }
        }

        RefererConfig<T> motanServiceReferer = new RefererConfig<T>();

        // 设置接口及实现类
        motanServiceReferer.setInterface(tClass);

        // 配置服务的group以及版本号
        motanServiceReferer.setGroup(group);
        motanServiceReferer.setVersion(rVersion);
        motanServiceReferer.setRequestTimeout(rRequestTimeout);
        motanServiceReferer.setRetries(rRetries);
        motanServiceReferer.setAccessLog(rAccessLog);
        motanServiceReferer.setCheck(rCheck);

        // 配置ZooKeeper注册中心
        RegistryConfig zookeeperRegistry = new RegistryConfig();
        zookeeperRegistry.setRegProtocol(zRegProtocol);
        zookeeperRegistry.setAddress(zAddress);
        zookeeperRegistry.setUsername(zUsername);
        zookeeperRegistry.setPassword(zPassword);
        zookeeperRegistry.setConnectTimeout(zConnectTimeout);
        motanServiceReferer.setRegistry(zookeeperRegistry);

        // 配置RPC协议
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setId(pId);
        protocol.setName(pName);
        protocol.setHaStrategy(pHaStrategy);
        protocol.setLoadbalance(pLoadbalance);
        protocol.setRequestTimeout(pRequestTimeout);
        protocol.setMaxClientConnection(pMaxClientConnection);
        protocol.setMinClientConnection(pMinClientConnection);
        motanServiceReferer.setProtocol(protocol);

        // 指定调用机器或者调用本地服务需添加此配置
        if(directUrl != null && !"".equals(directUrl))motanServiceReferer.setDirectUrl(directUrl);

        // 获取代理服务
        T service = motanServiceReferer.getRef();

        return service;

    }
}
