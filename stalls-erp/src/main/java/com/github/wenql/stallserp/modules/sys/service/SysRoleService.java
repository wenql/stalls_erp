package com.github.wenql.stallserp.modules.sys.service;

import com.github.wenql.stallserp.modules.sys.model.SysMenu;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.model.SysRole;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysRoleService extends IService<SysRole> {
 /**
     * 添加角色
     */
    boolean create(SysRole role);

    /**
     * 批量删除角色
     */
    boolean delete(List<Long> ids);

    /**
     * 分页获取角色列表
     */
    Page<SysRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<SysMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<SysMenu> listMenu(Long roleId);

    /**
     * 获取角色相关资源
     */
    List<SysResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);
}
