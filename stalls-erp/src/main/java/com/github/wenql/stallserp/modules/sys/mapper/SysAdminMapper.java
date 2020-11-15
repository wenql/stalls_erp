package com.github.wenql.stallserp.modules.sys.mapper;

import com.github.wenql.stallserp.modules.sys.model.SysAdmin;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);

}
