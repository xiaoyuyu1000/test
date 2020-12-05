package cn.appsys.service.impl;

import cn.appsys.dao.DevUserMapper;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
    @Autowired
    private DevUserMapper devUserMapper;


    public DevUser findDevUser(String devCode,String devPassword) {

        System.out.println("service1");
        return devUserMapper.findDevUser(devCode, devPassword);
    }

}
