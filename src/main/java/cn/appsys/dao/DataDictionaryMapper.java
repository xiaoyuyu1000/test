package cn.appsys.dao;

import cn.appsys.pojo.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDictionaryMapper {
    //查询角色名称
    DataDictionary find(@Param(value = "userType") int userType);
    public List<DataDictionary> getDataDictionaryList(@Param("typeCode")String typeCode)throws Exception;
}
