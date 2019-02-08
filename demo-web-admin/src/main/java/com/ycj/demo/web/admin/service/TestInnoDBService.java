package com.ycj.demo.web.admin.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ycj.demo.domain.TestInnoDB;
import com.ycj.demo.web.admin.dao.TestInnoDBMapper;
import com.ycj.demo.web.admin.vo.TestVo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TestInnoDBService extends BaseService<TestInnoDB, TestInnoDBMapper>{

    public int insertList(List<TestInnoDB> testList){
        return mapper.insertList(testList);
    }

    public Page<TestInnoDB> selectByExample(TestVo test, Integer page, Integer limit){
        PageHelper.startPage(page, limit);

        Example example = new Example(TestInnoDB.class);
        Example.Criteria criteria = example.createCriteria();

        if(StrUtil.isNotEmpty(test.getBirthdayStart())){
            criteria.andGreaterThan("birthday", DateUtil.parse(test.getBirthdayStart(), DatePattern.NORM_DATE_PATTERN));
        }

        if(StrUtil.isNotEmpty(test.getBirthdayEnd())){
            criteria.andLessThan("birthday", DateUtil.parse(test.getBirthdayEnd(), DatePattern.NORM_DATE_PATTERN));
        }

        if(StrUtil.isNotEmpty(test.getPhone())){
            criteria.andLike("phone", test.getPhone()+ '%');
        }

        if(StrUtil.isNotEmpty(test.getUsername())){
            criteria.andLike("username", test.getUsername()+ '%');
        }

        if(null != test.getSex()){
            criteria.andEqualTo("sex", test.getSex());
        }

        if(StrUtil.isNotEmpty(test.getAddress())){
            criteria.andLike("address",test.getAddress()+ '%');
        }

        if(StrUtil.isNotEmpty(test.getEmail())){
            criteria.andLike("email",test.getEmail()+ '%');
        }

        return (Page<TestInnoDB>) mapper.selectByExample(example);
    }

}
