package com.webservice;

import com.webservice.bean.Results;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
/**
 * Created by ziyu.zhang on 2016/12/9.
 * Description WebService 请求
 */
public class WebServiceClient {
    private static String  url        = "http://xxxx/axis2/services/xxx?wsdl";
    private static String  nameSpace  = "http://tempuri.org";
    private static String  methodName = "xxx";

    public static OMElement getSoapRequestMessage() {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = factory.createOMNamespace(nameSpace, "");
        OMElement param1 = factory.createOMElement("age", omNs);
        OMElement param2 = factory.createOMElement("name", omNs);
        param1.setText("10");
        param2.setText("Jerry");

        OMElement requestSoapMessage = factory.createOMElement(
                methodName, omNs);
        requestSoapMessage.addChild(param1);
        requestSoapMessage.addChild(param2);
        requestSoapMessage.build();
        return requestSoapMessage;
    }

    public static Results method1(String uid,String system) {
        EndpointReference targetEPR = new EndpointReference(url);
        OMElement requestSoapMessage = getSoapRequestMessage();

        Options options = new Options();
        options.setTo(targetEPR);
        ServiceClient sender = null;
        Results results = null;
        try {
            sender = new ServiceClient();
            sender.setOptions(options);
            OMElement element = sender.sendReceive(requestSoapMessage);
            Iterator its = element.getChildElements();
            OMElement om = null;
            while (its.hasNext()){
                om = (OMElement)its.next();
                if("return".equals(om.getLocalName())){
                    //TODO
                }
            }
            return results;

        } catch (AxisFault e) {
            System.out.println(e.getFaultReasonElement().toString());
        }
        return null;

    }
    //axis2
    public static Results method2(String strParam){
        Results results = null;
        try {
            RPCServiceClient ser = new RPCServiceClient ();
            Options options = ser.getOptions();
            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(url);
            options.setTo(targetEPR);
            options.setAction(nameSpace+"/"+methodName);
            // 指定参数值
            Object[] opAddEntryArgs = new Object[] { strParam };
            // 指定返回值的数据类型的Class对象
            Class[] classes = new Class[] { String.class };
            // 指定要调用的方法及WSDL文件的命名空间
            QName opAddEntry = new QName(nameSpace,methodName);
            // 调用并输出该方法的返回值
            Object[] str = ser.invokeBlocking(opAddEntry, opAddEntryArgs, classes);
            System.out.println("result == >"+str[0].toString());
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return results;
    }


    //axis2
    public static void method21(String strParam){
        OMElement result = null;
        try {
            Options options = new Options();
            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(url);
            options.setTo(targetEPR);
            // 设定调用方法名
            options.setAction(methodName);

            // Cookie设定
            StringBuffer cookieSb = new StringBuffer();
            options.setManageSession(true);
            cookieSb.append("Set-Cookie:");
            options.setProperty(HTTPConstants.COOKIE_STRING, cookieSb.toString());
            // 禁用HTTP传输分段特性,避免报错
            options.setProperty(HTTPConstants.CHUNKED, false);

            options.setSoapVersionURI(nameSpace+"/"+methodName);

            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMFactory fac = OMAbstractFactory.getOMFactory();
            // 命名空间，有时命名空间不增加没事，不过最好加上，因为有时有事，你懂的
            OMNamespace omNs = fac.createOMNamespace(url, methodName);

            OMElement method = fac.createOMElement("getName", omNs);
            OMElement symbol = fac.createOMElement("name", omNs);
            // symbol.setText("1");
            symbol.addChild(fac.createOMText(symbol, strParam));
            method.addChild(symbol);
            method.build();

            result = sender.sendReceive(method);

            System.out.println("result == > " + result);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

    }

    public static void method3(String strParam)throws AxisFault {
        OMElement result = null;
        try {
            Options options = new Options();
            EndpointReference targetEPR = new EndpointReference(url);
            options.setTo(targetEPR);
            options.setAction("xxx");
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNs = fac.createOMNamespace(url, methodName);
            OMElement method = fac.createOMElement("getName", omNs);
            OMElement symbol = fac.createOMElement("name", omNs);
            symbol.addChild(fac.createOMText(symbol, strParam));
            method.addChild(symbol);
            method.build();

            result = sender.sendReceive(method);
            System.out.println("result == > " + result);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

    }

    public static void method4(String strParam)throws AxisFault {
        Results results = null;
        // 使用RPC方式调用WebService
        RPCServiceClient serviceClient = new RPCServiceClient();
        // 指定调用WebService的URL
        EndpointReference targetEPR = new EndpointReference(url);
        Options options = serviceClient.getOptions();
        //确定目标服务地址
        options.setTo(targetEPR);
        //确定调用方法
        options.setAction(nameSpace+"/"+methodName);
        QName qname = new QName(nameSpace,methodName);
        //QName qname = new QName(nameSpace, methodName);
        // 指定getPrice方法的参数值
        Object[] parameters = new Object[] { strParam };

        // 指定getPrice方法返回值的数据类型的Class对象
        Class[] returnTypes = new Class[] { String.class };
        // 调用方法一 传递参数，调用服务，获取服务返回结果集
        OMElement element = serviceClient.invokeBlocking(qname, parameters);
        //值得注意的是，返回结果就是一段由OMElement对象封装的xml字符串。
        //我们可以对之灵活应用,下面我取第一个元素值，并打印之。因为调用的方法返回一个结果
        String result = element.getFirstElement().getText();
        System.out.println("result == > " + result);

    }

    public static void method5(String param)throws AxisFault {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(url);
            call.setOperationName(new QName(nameSpace,methodName));
            call.addParameter(new QName(nameSpace,"dataInfo"), XMLType.XSD_STRING, ParameterMode.IN);//接口的参数
            call.setUseSOAPAction(true);
            call.setReturnType(XMLType.SOAP_STRING);
            call.setSOAPActionURI(nameSpace+"/"+methodName);
            String result = (String)call.invoke(new Object[]{param});
            System.out.println("result == > " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void method6(){
        String requestParams = xmlReq();
        try{
            System.out.println("request params == > "+requestParams);
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Host", "xxx.xx.xxx.xx:8080");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(requestParams.length()));
            conn.setRequestProperty("SOAPAction", nameSpace+methodName);
            conn.setRequestMethod("POST");
            //定义输出流
            OutputStream output = conn.getOutputStream();
            if (null != requestParams) {
                byte[] b = requestParams.getBytes("utf-8");
            //发送soap请求报文
            output.write(b, 0, b.length);}
            output.flush();
            output.close();
            //定义输入流，获取soap响应报文
            InputStream input = conn.getInputStream();
            String s = IOUtils.toString(input, "UTF-8");
            input.close();
            System.out.println("输出的xml == > "+s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void method7() {
        try {

            byte[] requestBytes;
            String soapRequestInfo = "";
            requestBytes = soapRequestInfo.getBytes("utf-8");

            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(url);
            postMethod.setRequestHeader("SOAPAction", nameSpace+"/GetToken");

            InputStream inputStream = new ByteArrayInputStream(requestBytes, 0, requestBytes.length);
            RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, requestBytes.length, "application/soap+xml; charset=utf-8");
            postMethod.setRequestEntity(requestEntity);

            int state = httpClient.executeMethod(postMethod);

            InputStream soapResponseStream = postMethod.getResponseBodyAsStream();
            InputStreamReader inputStreamReader = new InputStreamReader(soapResponseStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String responseLine = "";
            String soapResponseInfo = "";
            while ((responseLine = bufferedReader.readLine()) != null) {
                soapResponseInfo = soapResponseInfo + responseLine;
                System.out.println(soapResponseInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String xmlReq() {
        StringBuffer sb = new StringBuffer();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">");
        sb.append("<soapenv:Header/>");
        sb.append("<soapenv:Body>");
        sb.append("<tem:xxx>");
        sb.append("<tem:param1>xxx</tem:param1>");
        sb.append("<tem:param2>xxx</tem:param2>");
        sb.append("</tem:xxx>");
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");
        return sb.toString();
    }

    /**
     * 从输入流中读取数据
     */
    public static byte[] readInputStream(InputStream inStream){
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = inStream.read(buffer)) !=-1 ){
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();//网页的二进制数据
            outStream.close();
            inStream.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
