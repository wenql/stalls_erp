package com.github.wenql.stallserp.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wenql.stallserp.common.exception.Asserts;
import com.github.wenql.stallserp.domain.AdminUserDetails;
import com.github.wenql.stallserp.modules.sys.dto.SysAdminParam;
import com.github.wenql.stallserp.modules.sys.dto.UpdateAdminPasswordParam;
import com.github.wenql.stallserp.modules.sys.mapper.SysAdminLoginLogMapper;
import com.github.wenql.stallserp.modules.sys.mapper.SysAdminMapper;
import com.github.wenql.stallserp.modules.sys.mapper.SysResourceMapper;
import com.github.wenql.stallserp.modules.sys.mapper.SysRoleMapper;
import com.github.wenql.stallserp.modules.sys.model.SysAdmin;
import com.github.wenql.stallserp.modules.sys.model.SysAdminLoginLog;
import com.github.wenql.stallserp.modules.sys.model.SysAdminRoleRelation;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.model.SysRole;
import com.github.wenql.stallserp.modules.sys.service.SysAdminCacheService;
import com.github.wenql.stallserp.modules.sys.service.SysAdminRoleRelationService;
import com.github.wenql.stallserp.modules.sys.service.SysAdminService;
import com.github.wenql.stallserp.security.util.JwtTokenUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysAdminLoginLogMapper loginLogMapper;
    @Autowired
    private SysAdminCacheService adminCacheService;
    @Autowired
    private SysAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysResourceMapper resourceMapper;

    @Override
    public SysAdmin getAdminByUsername(String username) {
        SysAdmin admin = adminCacheService.getAdmin(username);
        if (admin != null) {
            return admin;
        }
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdmin::getUsername, username);
        List<SysAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public SysAdmin register(SysAdminParam SysAdminParam) {
        SysAdmin admin = new SysAdmin();
        BeanUtils.copyProperties(SysAdminParam, admin);
        admin.setCreateTime(new Date());
        admin.setStatus(1);
        // 查询是否有相同用户名的用户
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdmin::getUsername, admin.getUsername());
        List<SysAdmin> SysAdminList = list(wrapper);
        if (SysAdminList.size() > 0) {
            return null;
        }
        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodePassword);
        baseMapper.insert(admin);
        return admin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        // 密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            // updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * 
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        SysAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            SysAdminLoginLog loginLog = new SysAdminLoginLog();
            loginLog.setAdminId(admin.getId());
            loginLog.setCreateTime(new Date());
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            loginLog.setIp(request.getRemoteAddr());
            loginLogMapper.insert(loginLog);
        }
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        SysAdmin record = new SysAdmin();
        record.setLoginTime(new Date());
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdmin::getUsername, username);
        update(record, wrapper);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public Page<SysAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SysAdmin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysAdmin> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(SysAdmin::getUsername, keyword);
            lambda.or().like(SysAdmin::getNickName, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, SysAdmin admin) {
        admin.setId(id);
        SysAdmin rawAdmin = getById(id);
        if (rawAdmin.getPassword().equals(admin.getPassword())) {
            // 与原加密密码相同的不需要修改
            admin.setPassword(null);
        } else {
            // 与原加密密码不同的需要加密修改
            if (StrUtil.isEmpty(admin.getPassword())) {
                admin.setPassword(null);
            } else {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        boolean success = updateById(admin);
        adminCacheService.delAdmin(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        adminCacheService.delAdmin(id);
        boolean success = removeById(id);
        adminCacheService.delResourceList(id);
        return success;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 先删除原来的关系
        QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdminRoleRelation::getAdminId, adminId);
        adminRoleRelationService.remove(wrapper);
        // 建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysAdminRoleRelation roleRelation = new SysAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
        adminCacheService.delResourceList(adminId);
        return count;
    }

    @Override
    public List<SysRole> getRoleList(Long adminId) {
        return roleMapper.getRoleList(adminId);
    }

    @Override
    public List<SysResource> getResourceList(Long adminId) {
        List<SysResource> resourceList = adminCacheService.getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)) {
            return resourceList;
        }
        resourceList = resourceMapper.getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)) {
            adminCacheService.setResourceList(adminId, resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if (StrUtil.isEmpty(param.getUsername()) || StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())) {
            return -1;
        }
        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdmin::getUsername, param.getUsername());
        List<SysAdmin> adminList = list(wrapper);
        if (CollUtil.isEmpty(adminList)) {
            return -2;
        }
        SysAdmin SysAdmin = adminList.get(0);
        if (!passwordEncoder.matches(param.getOldPassword(), SysAdmin.getPassword())) {
            return -3;
        }
        SysAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(SysAdmin);
        adminCacheService.delAdmin(SysAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 获取用户信息
        SysAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<SysResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
