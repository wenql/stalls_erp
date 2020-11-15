package com.github.wenql.stallserp.modules.sys.mapper;

import com.github.wenql.stallserp.modules.sys.model.SysResource;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {
    /**
     * 获取用户所有可访问资源
     */
    List<SysResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 根据角色ID获取资源
     */
    List<SysResource> getResourceListByRoleId(@Param("roleId") Long roleId);

}
