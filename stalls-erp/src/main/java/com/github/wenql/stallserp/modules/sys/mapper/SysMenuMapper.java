package com.github.wenql.stallserp.modules.sys.mapper;

import com.github.wenql.stallserp.modules.sys.model.SysMenu;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据后台用户ID获取菜单
     */
    List<SysMenu> getMenuList(@Param("adminId") Long adminId);

    /**
     * 根据角色ID获取菜单
     */
    List<SysMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
}
