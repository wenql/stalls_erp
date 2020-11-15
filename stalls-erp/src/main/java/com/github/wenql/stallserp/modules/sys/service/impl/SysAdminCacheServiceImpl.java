package com.github.wenql.stallserp.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wenql.stallserp.common.service.RedisService;
import com.github.wenql.stallserp.modules.sys.mapper.SysAdminMapper;
import com.github.wenql.stallserp.modules.sys.model.SysAdmin;
import com.github.wenql.stallserp.modules.sys.model.SysAdminRoleRelation;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.service.SysAdminCacheService;
import com.github.wenql.stallserp.modules.sys.service.SysAdminRoleRelationService;
import com.github.wenql.stallserp.modules.sys.service.SysAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户缓存管理Service实现类
 */
@Service
public class SysAdminCacheServiceImpl implements SysAdminCacheService {
    @Autowired
    private SysAdminService adminService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysAdminMapper adminMapper;
    @Autowired
    private SysAdminRoleRelationService adminRoleRelationService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {
        SysAdmin admin = adminService.getById(adminId);
        if (admin != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysAdminRoleRelation::getRoleId,roleId);
        List<SysAdminRoleRelation> relationList = adminRoleRelationService.list(wrapper);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        QueryWrapper<SysAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysAdminRoleRelation::getRoleId,roleIds);
        List<SysAdminRoleRelation> relationList = adminRoleRelationService.list(wrapper);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminMapper.getAdminIdList(resourceId);
        if (CollUtil.isNotEmpty(adminIdList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public SysAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (SysAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(SysAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public List<SysResource> getResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        return (List<SysResource>) redisService.get(key);
    }

    @Override
    public void setResourceList(Long adminId, List<SysResource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
