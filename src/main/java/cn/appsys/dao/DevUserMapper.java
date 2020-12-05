package cn.appsys.dao;

import cn.appsys.pojo.DevUser;
import org.apache.ibatis.annotations.Param;

public interface DevUserMapper {

     DevUser findDevUser(@Param(value="devCode")String devCode,@Param(value = "devPassword")String devPassword);
}
