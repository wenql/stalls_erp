package com.github.wenql.stallserp.modules.sys.service.impl;

import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.mapper.SysResourceMapper;
import com.github.wenql.stallserp.modules.sys.service.SysAdminCacheService;
import com.github.wenql.stallserp.modules.sys.service.SysResourceService;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {
    @Autowired
    private SysAdminCacheService adminCacheService;
    @Override
    public boolean create(SysResource SysResource) {
        SysResource.setCreateTime(new Date());
        return save(SysResource);
    }

    @Override
    public boolean update(Long id, SysResource SysResource) {
        SysResource.setId(id);
        boolean success = updateById(SysResource);
        adminCacheService.delResourceListByResource(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        boolean success = removeById(id);
        adminCacheService.delResourceListByResource(id);
        return success;
    }

    @Override
    public Page<SysResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        Page<SysResource> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysResource> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysResource> lambda = wrapper.lambda();
        if(categoryId!=null){
            lambda.eq(SysResource::getCategoryId,categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            lambda.like(SysResource::getName,nameKeyword);
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            lambda.like(SysResource::getUrl,urlKeyword);
        }
        return page(page,wrapper);
    }

}
