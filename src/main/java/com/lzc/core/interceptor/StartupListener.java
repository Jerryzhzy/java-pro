package com.lzc.core.interceptor;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 启动监听器
 * Created by ziyu.zhang on 2017/6/30 19:20
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>{
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("I'm not Root WebApplicationContext!!");
	}

}
