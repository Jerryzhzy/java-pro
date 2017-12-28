package com.lzc.core.interceptor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListener
 * Created by ziyu.zhang on 2017/6/30 19:21
 */
public class SessionListener implements HttpSessionListener {

    /**
     * 创建Session
     * @param se
     */
    public void sessionCreated(HttpSessionEvent se) {
    }
    public void sessionDestroyed(HttpSessionEvent se) {
    	if(se != null){
    		HttpSession session = se.getSession();
    		if(session!=null){
				//TODO
    		}
    	}
    }

}
