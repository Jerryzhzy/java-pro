package com.webservice.bean;

/**
 * Created by ziyu.zhang on 2017/1/19.
 * Description 下发雨人接口返回结果
 */
public class MemberyurenResult {
    public String billnum;                  //会员卡号
    public String returncode;               //操作状态返回 成功：1   失败：2
    public String messagecontent;           //操作结果说明

    public String getBillnum() {
        return billnum;
    }

    public void setBillnum(String billnum) {
        this.billnum = billnum;
    }


    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }
}
