package com.github.wenql.stallserp.modules.sys.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wenql.stallserp.common.api.CommonPage;
import com.github.wenql.stallserp.common.api.CommonResult;
import com.github.wenql.stallserp.modules.sys.dto.SysAdminLoginParam;
import com.github.wenql.stallserp.modules.sys.dto.SysAdminParam;
import com.github.wenql.stallserp.modules.sys.dto.UpdateAdminPasswordParam;
import com.github.wenql.stallserp.modules.sys.model.SysAdmin;
import com.github.wenql.stallserp.modules.sys.model.SysRole;
import com.github.wenql.stallserp.modules.sys.service.SysAdminService;
import com.github.wenql.stallserp.modules.sys.service.SysRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/Admin")
@Api(tags = "SysAdminController", description = "后台用户管理")
public class SysAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private SysAdminService adminService;
    @Autowired
    private SysRoleService roleService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<SysAdmin> register(@Validated @RequestBody SysAdminParam SysAdminParam) {
        SysAdmin SysAdmin = adminService.register(SysAdminParam);
        if (SysAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(SysAdmin);
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@Validated @RequestBody SysAdminLoginParam SysAdminLoginParam) {
        String token = adminService.login(SysAdminLoginParam.getUsername(), SysAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult getAdminInfo(Principal principal) {
        if(principal==null){
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        SysAdmin SysAdmin = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", SysAdmin.getUsername());
        data.put("menus", roleService.getMenuList(SysAdmin.getId()));
        data.put("icon", SysAdmin.getIcon());
        List<SysRole> roleList = adminService.getRoleList(SysAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(SysRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<SysAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysAdmin> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<SysAdmin> getItem(@PathVariable Long id) {
        SysAdmin admin = adminService.getById(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id, @RequestBody SysAdmin admin) {
        boolean success = adminService.update(id, admin);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        boolean success = adminService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public CommonResult updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        SysAdmin SysAdmin = new SysAdmin();
        SysAdmin.setStatus(status);
        boolean success = adminService.update(id,SysAdmin);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    public CommonResult<List<SysRole>> getRoleList(@PathVariable Long adminId) {
        List<SysRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }
}

