package com.atguigu.atcrowdfunding.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Lifecycle Listener implementation class StartUpServletContextListener
 *
 */
public class StartUpServletContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

	/**
	 * 准备项目的初始化参数
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent eve)  { 
    	//服务器启动后ServletContext对象创建成功后立即调用
    	eve.getServletContext().setAttribute("PATH",  eve.getServletContext().getContextPath());
    	
    }
	
}
