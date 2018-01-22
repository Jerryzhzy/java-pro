package com.webservice.bean;

/**
 * Created by ziyu.zhang on 2018/1/22.
 * Description 联系人CUD请求参数
 */
public class ContactReq {

    private String ccontactcode;        //客户联系人编码。必填。
    private String ccontactname;        //客户联系人名称。必填。
    private String ccuscode;            //所属客户编码，必填。即客户资料里面的客户编码。
    private String bcsexid;             //性别，可空。
    private String cprincipal;          //负责人 （业务员），可空。
    private String bmajor;              //是否主要联系人，必填。0 否，1 是。
    private String cmobilephone;        //移动电话，可空。
    private String cofficephone;        //办公电话，可空。
    private String chomephone;          //家庭电话  ，可空。
    private String cemail;              //电子邮件，可空。
    private String cmemo;               //备注，可空。


    public String getCcontactcode() {
        return ccontactcode;
    }

    public void setCcontactcode(String ccontactcode) {
        this.ccontactcode = ccontactcode;
    }

    public String getCcontactname() {
        return ccontactname;
    }

    public void setCcontactname(String ccontactname) {
        this.ccontactname = ccontactname;
    }

    public String getCcuscode() {
        return ccuscode;
    }

    public void setCcuscode(String ccuscode) {
        this.ccuscode = ccuscode;
    }

    public String getBcsexid() {
        return bcsexid;
    }

    public void setBcsexid(String bcsexid) {
        this.bcsexid = bcsexid;
    }

    public String getCprincipal() {
        return cprincipal;
    }

    public void setCprincipal(String cprincipal) {
        this.cprincipal = cprincipal;
    }

    public String getBmajor() {
        return bmajor;
    }

    public void setBmajor(String bmajor) {
        this.bmajor = bmajor;
    }

    public String getCmobilephone() {
        return cmobilephone;
    }

    public void setCmobilephone(String cmobilephone) {
        this.cmobilephone = cmobilephone;
    }

    public String getCofficephone() {
        return cofficephone;
    }

    public void setCofficephone(String cofficephone) {
        this.cofficephone = cofficephone;
    }

    public String getChomephone() {
        return chomephone;
    }

    public void setChomephone(String chomephone) {
        this.chomephone = chomephone;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public String getCmemo() {
        return cmemo;
    }

    public void setCmemo(String cmemo) {
        this.cmemo = cmemo;
    }
}
