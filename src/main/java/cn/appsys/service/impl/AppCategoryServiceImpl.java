package cn.appsys.service.impl;

import cn.appsys.dao.AppCategoryMapper;
import cn.appsys.dao.AppInfoMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.service.AppCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {
    @Autowired
    private AppCategoryMapper appCategoryMapper;
    @Override
    public List<AppCategory> getAppCategoryListByParentId(Integer parentId) throws Exception {
        return  appCategoryMapper.getAppCategoryListByParentId(parentId);
    }
}
