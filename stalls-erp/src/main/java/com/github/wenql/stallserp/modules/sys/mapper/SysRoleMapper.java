package com.github.wenql.stallserp.modules.sys.mapper;

import com.github.wenql.stallserp.modules.sys.model.SysRole;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 获取用户所有角色
     */
    List<SysRole> getRoleList(@Param("adminId") Long adminId);
}
