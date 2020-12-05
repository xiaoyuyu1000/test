package cn.appsys.service.impl;

import cn.appsys.dao.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dataDictionaryService")
@Transactional
public class DataDictionaryServiceImpl implements DataDictionaryService {
    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;
    @Override
    public DataDictionary findData(int userType) {
        DataDictionary dataDictionary=dataDictionaryMapper.find(userType);
        System.out.println(dataDictionary.toString());
        return dataDictionary;
    }
    @Override
    public List<DataDictionary> getDataDictionaryList(String typeCode)
            throws Exception {

        return dataDictionaryMapper.getDataDictionaryList(typeCode);
    }
}
