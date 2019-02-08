package com.ycj.demo.web.admin.service;


import com.github.pagehelper.PageHelper;
import com.ycj.demo.domain.BaseEntity;
import com.ycj.demo.web.admin.tkmybatis.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseService<T extends BaseEntity, D extends MyMapper<T>> {

    @Autowired
    protected D mapper;

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public T selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键删除
     * 遇到异常回滚
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id){
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询全部
     * @param page
     * @param limit
     * @return
     */
    public List<T> selectAll(Integer page, Integer limit){
        PageHelper.startPage(page, limit);
        return mapper.selectAll();
    }

    /**
     * 插入
     * @param entity
     * @return 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public int insert(T entity){
        mapper.insertUseGeneratedKeys(entity);
        return entity.getId();
    }

    /**
     * 修改  只更新不为空的字段 根据ID
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateSelective(T entity){
        return mapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 修改 所有字段 根据ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(T entity){
        return mapper.updateByPrimaryKey(entity);
    }

}
