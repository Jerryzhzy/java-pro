package com.webservice.bean;

/**
 * Created by ziyu.zhang on 2018/1/22.
 * Description 客户银行CUD参数
 */
public class CustomerBankReq {

    private String caccountnum;         //银行账号。必填。
    private String caccountname;        //帐号名称 。可空。
    private String ccuscode;            //所属客户编码，必填。即客户资料里面的客户编码。
    private String cbranch;             //开户银行，可空。
    private String cbank;               //所属银行 ，可空。
    private int bdefault;               //是否默认值，必填。0 否，1 是。


    public String getCaccountnum() {
        return caccountnum;
    }

    public void setCaccountnum(String caccountnum) {
        this.caccountnum = caccountnum;
    }

    public String getCaccountname() {
        return caccountname;
    }

    public void setCaccountname(String caccountname) {
        this.caccountname = caccountname;
    }

    public String getCcuscode() {
        return ccuscode;
    }

    public void setCcuscode(String ccuscode) {
        this.ccuscode = ccuscode;
    }

    public String getCbranch() {
        return cbranch;
    }

    public void setCbranch(String cbranch) {
        this.cbranch = cbranch;
    }

    public String getCbank() {
        return cbank;
    }

    public void setCbank(String cbank) {
        this.cbank = cbank;
    }

    public int getBdefault() {
        return bdefault;
    }

    public void setBdefault(int bdefault) {
        this.bdefault = bdefault;
    }
}
