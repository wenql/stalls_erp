package com.github.wenql.stallserp.modules.sys.service;

import com.github.wenql.stallserp.modules.sys.dto.SysMenuNode;
import com.github.wenql.stallserp.modules.sys.model.SysMenu;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysMenuService extends IService<SysMenu> {
 /**
     * 创建后台菜单
     */
    boolean create(SysMenu SysMenu);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, SysMenu SysMenu);

    /**
     * 分页查询后台菜单
     */
    Page<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<SysMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    boolean updateHidden(Long id, Integer hidden);

}
