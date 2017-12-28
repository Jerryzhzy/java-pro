package com.lzc.core.interceptor;


import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * SessionAttributeListener
 * Created by ziyu.zhang on 2017/6/30 19:22
 */
public class SessionAttributeListener implements HttpSessionAttributeListener {

    // 定义监听的session属性名.
    public final static String _USER_LOGIN_LOG_ = "_USER_LOGIN_LOG_";
    private static List<String> sessionids = new ArrayList<>();

    /**
     * 加入session时的监听
     */
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        try {
            String sessionId = sbe.getSession().getId();
            if (_USER_LOGIN_LOG_.equals(sbe.getName())) {
                // 判断存入的Id是否包含在list中，如果已包含则不添加
                if(null==getSessions() || getSessions().size() == 0 ){
                    sessionids.add(sessionId);
                    //将登录信息写入登录log

                }else{
                    // 判断存入的对象是否包含在list中，如果已包含则不添加
                    boolean isexist = true;
                    for (int i = 0; i < getSessions().size(); i++) {
                        if(null!=getSessions() && getSessions().get(i).equals(sessionId)){
                            isexist = false;
                            break;
                        }
                    }
                    if(isexist){
                        //将登录信息写入登录log
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * session失效时的监听
     */
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        if (_USER_LOGIN_LOG_.equals(sbe.getName())) {
            if(getSessions().contains(sbe.getSession().getId())) {
                sessionids.remove(sbe.getSession().getId());
                //写入Log 退出时间

            }
        }
    }
    /**
     * session覆盖时的监听方法.
     */
    public void attributeReplaced(HttpSessionBindingEvent sbe) {

    }
    /**
     * session的集合.
     */
    public static List<String> getSessions() {
        return sessionids;
    }

}
