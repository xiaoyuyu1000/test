package cn.appsys.service.impl;

import cn.appsys.dao.BackenUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.BackendUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("backendUserService")
@Transactional
public class BackendUserServiceImpl implements BackendUserService {
    @Autowired
    BackenUserMapper backenUserMapper;
    public BackendUser findBackendUser(String userCode,String userPassword) {
        System.out.println("bservice1");
        BackendUser backendUser=backenUserMapper.findBackendUser(userCode,userPassword);
        System.out.println("bservice2");
        return backendUser;
    }
}
