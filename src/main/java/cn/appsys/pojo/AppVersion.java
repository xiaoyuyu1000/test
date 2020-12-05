package cn.appsys.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * App版本信息表
 * @author xiaoyuying
 * @version 1.0
 */
public class AppVersion {
    private Integer id;
    private Integer appId;//app的id（外键app_info{id}）,
    private String versionNo;//版本编号
    private String versionInfo;//版本信息
    private Integer publishStatus;//发布状态，对应data_dictionary，1 不发布 2 已发布 3 预发布
    private String downloadLink;//下载链接
    private BigDecimal versionSize;//版本大小
    private Integer createdBy;//创建者id（外键dev_user{id}）
    private Date creationDate;//创建日期时间
    private Integer modifyBy;//修改者id（外键dev_user{id}）
    private Date modifyDate;//修改时间
    private String apkLocPath;//apk在服务器的存储路径
    private String apkFileName;//apk文件名
    private String appName;
    private String publishStatusName;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public BigDecimal getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(BigDecimal versionSize) {
        this.versionSize = versionSize;
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

    public String getApkLocPath() {
        return apkLocPath;
    }

    public void setApkLocPath(String apkLocPath) {
        this.apkLocPath = apkLocPath;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPublishStatusName() {
        return publishStatusName;
    }

    public void setPublishStatusName(String publishStatusName) {
        this.publishStatusName = publishStatusName;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "id=" + id +
                ", appId=" + appId +
                ", versionNo='" + versionNo + '\'' +
                ", versionInfo='" + versionInfo + '\'' +
                ", publishStatus=" + publishStatus +
                ", downloadLink='" + downloadLink + '\'' +
                ", versionSize=" + versionSize +
                ", createdBy=" + createdBy +
                ", creationDate=" + creationDate +
                ", modifyBy=" + modifyBy +
                ", modifyDate=" + modifyDate +
                ", apkLocPath='" + apkLocPath + '\'' +
                ", apkFileName='" + apkFileName + '\'' +
                ", appName='" + appName + '\'' +
                ", publishStatusName='" + publishStatusName + '\'' +
                '}';
    }
}
