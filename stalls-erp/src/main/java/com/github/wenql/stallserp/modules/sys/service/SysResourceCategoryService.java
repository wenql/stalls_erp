package com.github.wenql.stallserp.modules.sys.service;

import com.github.wenql.stallserp.modules.sys.model.SysResourceCategory;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资源分类表 服务类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysResourceCategoryService extends IService<SysResourceCategory> {

    /**
     * 获取所有资源分类
     */
    List<SysResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    boolean create(SysResourceCategory SysResourceCategory);

}
