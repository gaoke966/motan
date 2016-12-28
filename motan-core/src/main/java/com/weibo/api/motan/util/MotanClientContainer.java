package com.weibo.api.motan.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kegao on 2016/12/26.
 */
public class MotanClientContainer extends ConcurrentHashMap {

    private MotanClientContainer(){}

    private static class SingletonHolder{
        private static MotanClientContainer instance = new MotanClientContainer();
    }

    public static  MotanClientContainer getInstance(){
        return SingletonHolder.instance;
    }
}
