package com.lzc.gstore.vocabulary.controller;

import com.lzc.core.datasource.controller.BaseController;
import com.lzc.gstore.vocabulary.entity.Demo;
import com.lzc.gstore.vocabulary.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ziyu.zhang on 2017/6/30.
 * Description 请用一句话描述
 */
@Controller
@RequestMapping(value="/vocabulary")
public class DemoController extends BaseController<DemoService, Demo, String>{

    private static final long serialVersionUID = 3329389870663465872L;


    @RequestMapping("/fileDownload")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response){
        //获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
        String filePath = request.getParameter("filePath");
        String webPath =  request.getRealPath("/");

        //TODO 文件加工处理
        //如果目录不存在则创建
        if (!(new File(webPath+filePath).exists())) {
            if (!(new File(webPath+filePath).mkdirs())) {
                return;
            }
        }

        /***
         * 文件输出
         */
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
        response.setHeader("Content-Disposition", "attachment;fileName="+filePath);
        ServletOutputStream out;
        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        File file = new File("c:\\"+filePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);

            //3.通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();

            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b=inputStream.read(buffer))!=-1){
                //4.写到输出流(out)中
                out.write(buffer,0,b);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value="/demo")
    public ModelAndView demo(ModelAndView mav,String test){
        System.out.println(test);
        mav.setViewName("examples/login/login");
        return mav;
    }


}
