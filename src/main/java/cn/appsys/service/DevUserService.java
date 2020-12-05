package cn.appsys.service;

import cn.appsys.pojo.DevUser;

public interface DevUserService {
    public DevUser findDevUser(String devCode,String devPassword);
}
