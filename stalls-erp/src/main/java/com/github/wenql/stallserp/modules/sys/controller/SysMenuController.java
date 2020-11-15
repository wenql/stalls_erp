package com.github.wenql.stallserp.modules.sys.controller;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wenql.stallserp.common.api.CommonPage;
import com.github.wenql.stallserp.common.api.CommonResult;
import com.github.wenql.stallserp.modules.sys.dto.SysMenuNode;
import com.github.wenql.stallserp.modules.sys.model.SysMenu;
import com.github.wenql.stallserp.modules.sys.service.SysMenuService;

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
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/Menu")
@Api(tags = "SysMenuController", description = "后台菜单管理")
public class SysMenuController {
    @Autowired
    private SysMenuService menuService;

    @ApiOperation("添加后台菜单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody SysMenu SysMenu) {
        boolean success = menuService.create(SysMenu);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改后台菜单")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id,
                               @RequestBody SysMenu SysMenu) {
        boolean success = menuService.update(id, SysMenu);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID获取菜单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<SysMenu> getItem(@PathVariable Long id) {
        SysMenu SysMenu = menuService.getById(id);
        return CommonResult.success(SysMenu);
    }

    @ApiOperation("根据ID删除后台菜单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页查询后台菜单")
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public CommonResult<CommonPage<SysMenu>> list(@PathVariable Long parentId,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysMenu> menuList = menuService.list(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(menuList));
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public CommonResult<List<SysMenuNode>> treeList() {
        List<SysMenuNode> list = menuService.treeList();
        return CommonResult.success(list);
    }

    @ApiOperation("修改菜单显示状态")
    @RequestMapping(value = "/updateHidden/{id}", method = RequestMethod.POST)
    public CommonResult updateHidden(@PathVariable Long id, @RequestParam("hidden") Integer hidden) {
        boolean success = menuService.updateHidden(id, hidden);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

}

