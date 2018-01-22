package com.webservice.bean;

/**
 * Created by zhangziyu on 17/6/2.
 */
public class Results {
    private String code;
    private String message;
    private String result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message){
        this.message = message;

    }
    public String getMessage(){
        return this.message;
    }
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }




}
