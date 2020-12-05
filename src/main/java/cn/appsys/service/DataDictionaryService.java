package cn.appsys.service;

import cn.appsys.pojo.DataDictionary;

import java.util.List;

public interface DataDictionaryService {
    //查询角色名称
    DataDictionary findData(int userType);
    public List<DataDictionary> getDataDictionaryList(String typeCode)throws Exception;
}
