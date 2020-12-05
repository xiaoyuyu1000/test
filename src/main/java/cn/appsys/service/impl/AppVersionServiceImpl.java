package cn.appsys.service.impl;

import cn.appsys.dao.AppInfoMapper;
import cn.appsys.dao.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {
    @Autowired

    private AppVersionMapper appVersionMapper;
    @Autowired
    private AppInfoMapper appInfoMapper;
    @Override
    public AppVersion getAppVersionById(Integer id) throws Exception {

        return appVersionMapper.getAppVersionById(id);
    }
    @Override
    public List<AppVersion> getAppVersionList(Integer appId) throws Exception {

        return appVersionMapper.getAppVersionList(appId);
    }
    @Override
    public boolean modify(AppVersion appVersion) throws Exception {
        // TODO Auto-generated method stub
        boolean flag = false;
        if(appVersionMapper.modify(appVersion) > 0){
            flag = true;
        }
        return flag;
    }
    /**
     * 业务：新增app的版本信息
     * 1、app_verion表插入数据
     * 2、更新app_info表对应app的versionId字段（记录最新版本id）
     * 注意：事务控制
     */
    @Override
    public boolean appsysadd(AppVersion appVersion) throws Exception {
        // TODO Auto-generated method stub
        boolean flag = false;
        Integer versionId = null;
        if(appVersionMapper.add(appVersion) > 0){
            versionId = appVersion.getId();
            flag = true;
        }
        if(appInfoMapper.updateVersionId(versionId, appVersion.getAppId()) > 0 && flag){
            flag = true;
        }
        return flag;
    }
}
