package com.github.wenql.stallserp.modules.sys.controller;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wenql.stallserp.common.api.CommonPage;
import com.github.wenql.stallserp.common.api.CommonResult;
import com.github.wenql.stallserp.modules.sys.model.SysMenu;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.model.SysRole;
import com.github.wenql.stallserp.modules.sys.service.SysRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/Role")
@Api(tags = "SysRoleController", description = "后台用户角色管理")
public class SysRoleController {
    @Autowired
    private SysRoleService roleService;

    @ApiOperation("添加角色")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody SysRole role) {
        boolean success = roleService.create(role);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        boolean success = roleService.updateById(role);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean success = roleService.delete(ids);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }


    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<SysRole>> listAll() {
        List<SysRole> roleList = roleService.list();
        return CommonResult.success(roleList);
    }

    @ApiOperation("根据角色名称分页获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<SysRole>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysRole> roleList = roleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @ApiOperation("修改角色状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        SysRole SysRole = new SysRole();
        SysRole.setId(id);
        SysRole.setStatus(status);
        boolean success = roleService.updateById(SysRole);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取角色相关菜单")
    @RequestMapping(value = "/listMenu/{roleId}", method = RequestMethod.GET)
    public CommonResult<List<SysMenu>> listMenu(@PathVariable Long roleId) {
        List<SysMenu> roleList = roleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("获取角色相关资源")
    @RequestMapping(value = "/listResource/{roleId}", method = RequestMethod.GET)
    public CommonResult<List<SysResource>> listResource(@PathVariable Long roleId) {
        List<SysResource> roleList = roleService.listResource(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给角色分配菜单")
    @RequestMapping(value = "/allocMenu", method = RequestMethod.POST)
    public CommonResult allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        int count = roleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @ApiOperation("给角色分配资源")
    @RequestMapping(value = "/allocResource", method = RequestMethod.POST)
    public CommonResult allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        int count = roleService.allocResource(roleId, resourceIds);
        return CommonResult.success(count);
    }
}

