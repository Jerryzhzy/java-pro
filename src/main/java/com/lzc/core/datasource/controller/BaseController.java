package com.lzc.core.datasource.controller;

import com.lzc.core.datasource.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by ziyu.zhang on 2017/6/30.
 * Description 基础控制层
 */
public abstract class BaseController<R extends BaseService<T, ID>, T, ID extends Serializable> implements Serializable{


    @Autowired
    protected R baseService;//默认注入业务层Service


    /**
     * @Description: 登录成功后进入欢迎页面
     */
    @RequestMapping(value="/welcome")
    public ModelAndView welcome(ModelAndView mav, ModelMap mm, HttpServletRequest request){
        HttpSession session =request.getSession();
        String username = request.getRemoteUser();
        mav.setViewName("/home/welcome");
        return mav;
    }

}
