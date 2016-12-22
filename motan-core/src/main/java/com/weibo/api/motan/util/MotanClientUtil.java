package com.weibo.api.motan.util;

import com.alibaba.fastjson.JSONObject;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kegao on 2016/12/20.
 */
public class MotanClientUtil {

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
     * @param configPath:指定外部文件路径读取配置属性
     * @param <T>
     * @return T
     */
    public static<T> T getMotanServiceReferer(Class<T> tClass, String group, String directUrl, String configPath) {

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

        if(configPath != null && !"".equals(configPath)) {
            Configuration configuration = new Configuration(configPath);
            if (configuration.getProperties().size() > 0) {
                if (!"".equals(configuration.getValue("rVersion"))) rVersion = configuration.getValue("rVersion");
                if (!"".equals(configuration.getValue("rRequestTimeout"))) rRequestTimeout = new Integer(configuration.getValue("rRequestTimeout"));
                if (!"".equals(configuration.getValue("rRetries"))) rRetries = new Integer(configuration.getValue("rRetries"));
                if (!"".equals(configuration.getValue("rAccessLog"))) rAccessLog = configuration.getValue("rAccessLog");
                if (!"".equals(configuration.getValue("rCheck"))) rCheck = configuration.getValue("rCheck");
                if (!"".equals(configuration.getValue("zRegProtocol"))) zRegProtocol = configuration.getValue("zRegProtocol");
                if (!"".equals(configuration.getValue("zAddress"))) zAddress = configuration.getValue("zAddress");
                if (!"".equals(configuration.getValue("zUsername"))) zUsername = configuration.getValue("zUsername");
                if (!"".equals(configuration.getValue("zConnectTimeout"))) zConnectTimeout = new Integer(configuration.getValue("zConnectTimeout"));
                if (!"".equals(configuration.getValue("pId"))) pId = configuration.getValue("pId");
                if (!"".equals(configuration.getValue("pName"))) pName = configuration.getValue("pName");
                if (!"".equals(configuration.getValue("pHaStrategy"))) pHaStrategy = configuration.getValue("pHaStrategy");
                if (!"".equals(configuration.getValue("pLoadbalance"))) pLoadbalance = configuration.getValue("pLoadbalance");
                if (!"".equals(configuration.getValue("pRequestTimeout"))) pRequestTimeout = new Integer(configuration.getValue("pRequestTimeout"));
                if (!"".equals(configuration.getValue("pMaxClientConnection"))) pMaxClientConnection = new Integer(configuration.getValue("pMaxClientConnection"));
                if (!"".equals(configuration.getValue("pMinClientConnection"))) pMinClientConnection = new Integer(configuration.getValue("pMinClientConnection"));
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
