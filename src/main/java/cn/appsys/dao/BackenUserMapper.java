package cn.appsys.dao;

import cn.appsys.pojo.BackendUser;
import org.apache.ibatis.annotations.Param;

public interface BackenUserMapper {
    public BackendUser findBackendUser(@Param(value = "userCode") String userCode,@Param(value = "userPassword") String userPassword);
}
