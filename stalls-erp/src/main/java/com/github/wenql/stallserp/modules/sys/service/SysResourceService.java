package com.github.wenql.stallserp.modules.sys.service;

import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysResourceService extends IService<SysResource> {
 /**
     * 添加资源
     */
    boolean create(SysResource SysResource);

    /**
     * 修改资源
     */
    boolean update(Long id, SysResource SysResource);

    /**
     * 删除资源
     */
    boolean delete(Long id);

    /**
     * 分页查询资源
     */
    Page<SysResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

}
