package cn.appsys.pojo;

import java.util.Date;

public class DevUser {
    //开发者表
    private Integer id;
    private String devCode;//账号
    private String devName;//姓名
    private String devPassword;//密码
    private String devEmail;//电子邮箱
    private String devInfo;//介绍
    private Integer createdBy;//谁创建的（外键backen_user{id})
    private Date creationDate;//注册时间
    private Integer modifyBy;//谁修改的（外键backen_user{id})
    private Date modifyDate;//最近信息修改时间
    private BackendUser backendUser;//关联管理员表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevPassword() {
        return devPassword;
    }

    public void setDevPassword(String devPassword) {
        this.devPassword = devPassword;
    }

    public String getDevEmail() {
        return devEmail;
    }

    public void setDevEmail(String devEmail) {
        this.devEmail = devEmail;
    }

    public String getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(String devInfo) {
        this.devInfo = devInfo;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BackendUser getBackendUser() {
        return backendUser;
    }

    public void setBackendUser(BackendUser backendUser) {
        this.backendUser = backendUser;
    }

    @Override
    public String toString() {
        return "DevUser{" +
                "id=" + id +
                ", devCode='" + devCode + '\'' +
                ", devName='" + devName + '\'' +
                ", devPassword='" + devPassword + '\'' +
                ", devEmail='" + devEmail + '\'' +
                ", devInfo='" + devInfo + '\'' +
                ", createdBy=" + createdBy +
                ", creationDate=" + creationDate +
                ", modifyBy=" + modifyBy +
                ", modifyDate=" + modifyDate +
                ", backendUser=" + backendUser +
                '}';
    }
}
