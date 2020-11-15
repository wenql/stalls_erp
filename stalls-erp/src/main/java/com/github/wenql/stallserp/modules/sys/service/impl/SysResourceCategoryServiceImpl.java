package com.github.wenql.stallserp.modules.sys.service.impl;

import com.github.wenql.stallserp.modules.sys.model.SysResourceCategory;
import com.github.wenql.stallserp.modules.sys.mapper.SysResourceCategoryMapper;
import com.github.wenql.stallserp.modules.sys.service.SysResourceCategoryService;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源分类表 服务实现类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Service
public class SysResourceCategoryServiceImpl extends ServiceImpl<SysResourceCategoryMapper, SysResourceCategory> implements SysResourceCategoryService {

    @Override
    public List<SysResourceCategory> listAll() {
        QueryWrapper<SysResourceCategory> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(SysResourceCategory::getSort);
        return list(wrapper);
    }

    @Override
    public boolean create(SysResourceCategory SysResourceCategory) {
        SysResourceCategory.setCreateTime(new Date());
        return save(SysResourceCategory);
    }

}
