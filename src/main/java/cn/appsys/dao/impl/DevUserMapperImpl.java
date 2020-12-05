package cn.appsys.dao.impl;

import cn.appsys.dao.DevUserMapper;
import cn.appsys.pojo.DevUser;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class DevUserMapperImpl implements DevUserMapper {
    private SqlSessionTemplate sqlSession;
    public DevUser findDevUser(String devCode,String devPassword) {
        return sqlSession.selectOne("cn.appsys.dao.DevUserMapper.findDevUser",devCode);
    }
}
