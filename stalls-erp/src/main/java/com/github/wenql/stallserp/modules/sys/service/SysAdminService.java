package com.github.wenql.stallserp.modules.sys.service;

import com.github.wenql.stallserp.modules.sys.dto.SysAdminParam;
import com.github.wenql.stallserp.modules.sys.dto.UpdateAdminPasswordParam;
import com.github.wenql.stallserp.modules.sys.model.SysAdmin;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.model.SysRole;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
public interface SysAdminService extends IService<SysAdmin> {
/**
     * 根据用户名获取后台管理员
     */
    SysAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    SysAdmin register(SysAdminParam SysAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户名或昵称分页查询用户
     */
    Page<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    boolean update(Long id, SysAdmin admin);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     */
    List<SysRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<SysResource> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);
}
