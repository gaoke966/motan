package com.weibo.api.motan.util;

import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kegao on 2016/12/26.
 */
public class MotanSingletonClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(MotanSingletonClientUtil.class);
    /**
     * 获取motan服务接口的代理实例
     * @param tClass：XxxApi.class
     * @param group:server端对应组
     * @param <T>
     * @return T
     */
    public static<T> T getMotanServiceReferer(Class<T> tClass, String group) {
        return getMotanServiceReferer(tClass, group, null);
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
        long begin = System.currentTimeMillis();

        //ServiceReferer默认配置
        String rVersion = "1.0";
        Integer rRequestTimeout = 3000;
        Integer rRetries = 0;
        String rAccessLog = "false";
        String rCheck = "true";
        String rThrowException = "true";

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
        String pSerialization = "hprose";

        if (MotanClientConfig.getProperties().size() > 0) {
            if (MotanClientConfig.getValue("rVersion") != null) rVersion = MotanClientConfig.getValue("rVersion");
            if (MotanClientConfig.getValue("rRequestTimeout") != null) rRequestTimeout = new Integer(MotanClientConfig.getValue("rRequestTimeout"));
            if (MotanClientConfig.getValue("rRetries") != null) rRetries = new Integer(MotanClientConfig.getValue("rRetries"));
            if (MotanClientConfig.getValue("rAccessLog") != null) rAccessLog = MotanClientConfig.getValue("rAccessLog");
            if (MotanClientConfig.getValue("rCheck") != null) rCheck = MotanClientConfig.getValue("rCheck");
            if (MotanClientConfig.getValue("rThrowException") != null) rThrowException = MotanClientConfig.getValue("rThrowException");

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
            if (MotanClientConfig.getValue("pSerialization") != null) pSerialization = MotanClientConfig.getValue("pSerialization");


        }else{
            logger.info("读取默认属性文件--->失败,将使用默认值！- 原因：文件名motanclient.properties错误或者文件不存在！");
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
        motanServiceReferer.setThrowException(new Boolean(rThrowException));

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
        if(pSerialization != null && !"".equals(pSerialization))protocol.setSerialization(pSerialization);
        motanServiceReferer.setProtocol(protocol);

        // 指定调用机器或者调用本地服务需添加此配置
        if(directUrl != null && !"".equals(directUrl))motanServiceReferer.setDirectUrl(directUrl);

        T service;
        String key = group+"|"+tClass.getName();
        MotanClientContainer motanClientContainer = MotanClientContainer.getInstance();
        if(motanClientContainer.containsKey(key)){
            service = (T) motanClientContainer.get(key);
            logger.info("motanClientContainer.containsKey("+key+")");
        }else {
            // 获取代理服务
            service = motanServiceReferer.getRef();
            motanClientContainer.put(key,service);
            logger.info("motanClientContainer.notContainsKey("+key+")");
        }
        logger.info("motanServiceReferer.getRef.cost"+(System.currentTimeMillis()-begin));
        return service;

    }
}
