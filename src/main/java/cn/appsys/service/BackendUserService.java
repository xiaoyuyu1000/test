package cn.appsys.service;

import cn.appsys.pojo.BackendUser;

public interface BackendUserService {
    public BackendUser findBackendUser(String userCode,String userPassword);
}
