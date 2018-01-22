package com.webservice.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ziyu.zhang on 2016/12/9.
 * Description Xml处理帮助工具
 */
public class XmlHandle {
    private static final Log log = LogFactory.getLog(XmlHandle.class);

    /**
     * List<String> stringList  ， 元素格式为 "name = 王五" ，  "age = 12"
     */
    public static String getDocumentXmlString(String rootElement ,  List<String> stringList){
        Element root = new Element(rootElement);// 创建根节点 SubmitOrder;
        Document Doc = new Document(root); //根节点添加到文档中；
        for (String string : stringList) {
            String[] split = string.split("=");
            root.addContent(new Element(split[0].trim()).setText(split[1].trim()));
        }
        return getChineseXml(Doc);
    }

    /**
     *
     * 通过对象获取该对象的XML格式的String
     */
    public static String getDocumentXmlString(Object obj){
        Element root = new Element(obj.getClass().getSimpleName());// 创建根节点 SubmitOrder;
        Document Doc = new Document(root);
        Field[] fields = obj.getClass().getDeclaredFields();
        handle(obj,fields,root);
        return getChineseXml(Doc);
    }


    public static void handle(Object obj,Field[] fields,Element root){
        for (Field field : fields) {
            try {
                if(field.get(obj)!=null && field.get(obj).toString()!=null && !"".equals(field.get(obj).toString())){
                    root.addContent(new Element(field.getName()).setText(field.get(obj).toString()));
                }else{
                    root.addContent(new Element(field.getName()));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * 通过对象获取该对象的XML格式的String
     */
    public static String getDocumentXmlString(Object obj , String rootElement){
        Element root = new Element(rootElement);// 创建根节点 SubmitOrder;
        Document Doc = new Document(root);
        Field[] fields = obj.getClass().getDeclaredFields();
        handle(obj,fields,root);
        return getChineseXml(Doc);
    }

    /**
     * 通过xml类型的String 来给对象赋值    （简单对象，没有嵌套 ）
     */
    public static Object getObject(Class clazz  , String text){
        StringReader read = new StringReader(text);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            Object obj = clazz.newInstance();
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element rootElement = doc.getRootElement();
//			System.out.println(rootElement.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
//			List jiedian = rootElement.getChildren();
            Field[] fields = clazz.getFields();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")){
                    for (Field field : fields) {
                        if(field.getName().toLowerCase().equals(method.getName().substring(3).toLowerCase())){
                            String context ="";
                            String nameUpper = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                            String nameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1);
                            if(rootElement.getChild(nameUpper)!=null){
                                context =  rootElement.getChildText(nameUpper);
                            }else if(rootElement.getChild(nameLower)!=null){
                                context =  rootElement.getChildText(nameLower);
                            }
                            try {
                                method.invoke(obj,context);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return obj;
            //获得XML中的命名空间（XML中未定义可不写）
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入对象 ， 及xml格式的字符串  ， 来得到这个对象的List 的集合
     */
    public static List getObjectList(Class clazz , String xml){
        List list = new LinkedList();
        StringReader read = new StringReader(xml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            Element child = root.getChild(clazz.getSimpleName()+"s");
            if(child!=null){
                List elements = child.getChildren(clazz.getSimpleName());
                if(elements!=null && elements.size()>0){
                    Method[] methods = clazz.getDeclaredMethods();
                    Field[] fields = clazz.getDeclaredFields();
                    for (int i = 0; i < elements.size(); i++) {
                        Object object = clazz.newInstance();
                        for (Method method : methods) {
                            if(method.getName().startsWith("set")){
                                for (Field field : fields) {
                                    if(method.getName().substring(3).toLowerCase().equals(field.getName().toLowerCase())){
                                        Element element = (Element) elements.get(i);
                                        try {
                                            String nameUpper = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                            String nameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1);
                                            if(element.getChild(nameUpper)!=null){
                                                method.invoke(object, element.getChildText(nameUpper));
                                            }else if(element.getChild(nameLower)!=null){
                                                method.invoke(object, element.getChildText(nameLower));
                                            }
                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                        } catch (IllegalAccessException e) {
                                            System.out.println(e.getMessage());
                                        } catch (InvocationTargetException e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                }

                            }
                        }
                        list.add(object);
                    }
                }
                return list;
            }else{
                return list;
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List getObjectList(Class clazz , Document document){
        List list = new LinkedList();
        //取的根元素
        Element root = document.getRootElement();
        Element child = root.getChild(clazz.getSimpleName()+"s");
        if(child!=null){
            List elements = child.getChildren(clazz.getSimpleName());
            if(elements!=null && elements.size()>0){
                Method[] methods = clazz.getDeclaredMethods();
                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < elements.size(); i++) {
                    Object object = null;
                    try {
                        object = clazz.newInstance();
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                    for (Method method : methods) {
                        if(method.getName().startsWith("set")){
                            for (Field field : fields) {
                                if(method.getName().substring(3).toLowerCase().equals(field.getName().toLowerCase())){
                                    Element element = (Element) elements.get(i);
                                    try {
                                        String nameUpper = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                        String nameLower = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1);
                                        if(element.getChild(nameUpper)!=null){
                                            method.invoke(object, element.getChildText(nameUpper));
                                        }else if(element.getChild(nameLower)!=null){
                                            method.invoke(object, element.getChildText(nameLower));
                                        }
                                    } catch (IllegalArgumentException e) {
                                        System.out.println(e.getMessage());
                                    } catch (IllegalAccessException e) {
                                        System.out.println(e.getMessage());
                                    } catch (InvocationTargetException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }

                        }
                    }
                    list.add(object);
                }
            }
            return list;
        }else{
            return list;
        }

    }

    /**
     * 通过返回的  xml类型的String 来给对象赋值    （简单对象，没有嵌套  , xml 元素名 = 对象属性名）
     */
    public static Object getObjectOa(Class clazz  , String text){
        try{
            Object entity=clazz.newInstance();
            StringReader read = new StringReader(text);
            //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            //创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            //通过输入源构造一个Document
            Document doc = null;
            try {
                doc = sb.build(source);
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element rootElement = doc.getRootElement();
            Field[] fields = clazz.getFields();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")){
                    for (Field field : fields) {
                        if(field.getName().toLowerCase().equals(method.getName().substring(3).toLowerCase())){
                            String context ="";
                            if(rootElement.getChild(field.getName())!=null){
                                context =  rootElement.getChildText(field.getName());
                            }
                            try {
                                method.invoke(entity,context);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return entity;
        } catch (InstantiationException e1) {
            System.out.println("clazz.newInstance() 失败");
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            System.out.println("clazz.newInstance() 失败");
            e1.printStackTrace();
        }
        return null;


    }

    public static String getChineseXml(Document Doc){
        Format format = Format.getPrettyFormat();
        format.setEncoding("GB2312");//设置编码格式
        StringWriter out=null; //输出对象
        String sReturn = ""; //输出字符串
        XMLOutputter outputter =new XMLOutputter();
        out=new StringWriter();
        try {
            outputter.output(Doc,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(null!=out){
            sReturn=out.toString();
        }
        String re = "";
        if(!"".equals(sReturn)){
            //去除换行
            Pattern p = Pattern.compile("\\s*\n");
            Matcher m = p.matcher(sReturn);
            sReturn = m.replaceAll("");
            re = sReturn.replace("GB2312" ,"UTF-8");//转换编码格式
        }
        return re;
    }
}
