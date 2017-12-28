package com.lzc.core.datasource.aop;

import com.lzc.core.datasource.DataSourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import java.lang.reflect.Method;
import com.lzc.core.datasource.annotation.DataSource;
/**
 * 读写分离切换数据源实现AOP
 * Created by ziyu.zhang on 2017/6/30 19:41
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice{
	private static Logger logger = LoggerFactory.getLogger(DataSourceAdvice.class);
	/* (non-Javadoc)
	* <p>Title: afterReturning</p>
	* <p>Description: service方法执行完之后被调用 </p>
	* @param arg0
	* @param method
	* @param args
	* @param target
	* @throws Throwable
	*/
	@Override
	public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	* <p>Title: before</p>
	* <p>Description: service方法执行之前被调用 </p>
	* @param method
	* @param args
	* @param target
	* @throws Throwable
	*/
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		logger.info("切入点: " + target.getClass().getName() + "类中" + method.getName() + "方法");
		DataSource dataSource = method.getAnnotation(DataSource.class);
		if(dataSource!=null&&dataSource.value()!=null&&dataSource.value().equals(DataSourceEntry.SourceType.SLAVE)){
			logger.info("切换到: slave");  
            DataSourceSwitcher.setSlave();
		}else{
			logger.info("切换到: master");  
	    	DataSourceSwitcher.setMaster();  
		}
	}
	
	/**
	 * 抛出Exception之后被调用
	 * @param method
	 * @param args
	 * @param target
	 * @param ex
	 * @throws Throwable
	 */
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {  
        DataSourceSwitcher.setMaster();
        logger.error("出现异常,切换到: master");  
        ex.printStackTrace();
    } 

}
