package cn.appsys.pojo;

import java.util.Date;

/**
 *广告宣传表
 * @author xiaoyuying
 */
public class AppPromotion {
    private Integer id;//主键
    private Integer appId;//外键appInfo id
    private String adPicPath;//图片路径
    private String adPV;//广告点击量
    private Integer carouseIPosition;//轮番位（1-n)
    private Date startTime;//起效时间
    private Date endTime;//失效时间
    private Integer createBy;//创建者来源于backend_user
    private Date creationDate;//创建时间
    private Integer modifyBy;//更新者来源于backend_user
    private Date modifyDate;//最新更新时间

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

    public String getAdPicPath() {
        return adPicPath;
    }

    public void setAdPicPath(String adPicPath) {
        this.adPicPath = adPicPath;
    }

    public String getAdPV() {
        return adPV;
    }

    public void setAdPV(String adPV) {
        this.adPV = adPV;
    }

    public Integer getCarouseIPosition() {
        return carouseIPosition;
    }

    public void setCarouseIPosition(Integer carouseIPosition) {
        this.carouseIPosition = carouseIPosition;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
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

    @Override
    public String toString() {
        return "AppPromotion{" +
                "id=" + id +
                ", appId=" + appId +
                ", adPicPath='" + adPicPath + '\'' +
                ", adPV='" + adPV + '\'' +
                ", carouseIPosition=" + carouseIPosition +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createBy=" + createBy +
                ", creationDate=" + creationDate +
                ", modifyBy=" + modifyBy +
                ", modifyDate=" + modifyDate +
                '}';
    }
}
