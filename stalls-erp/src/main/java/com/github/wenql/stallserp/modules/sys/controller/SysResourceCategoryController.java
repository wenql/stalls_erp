package com.github.wenql.stallserp.modules.sys.controller;

import java.util.List;

import com.github.wenql.stallserp.common.api.CommonResult;
import com.github.wenql.stallserp.modules.sys.model.SysResourceCategory;
import com.github.wenql.stallserp.modules.sys.service.SysResourceCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/ResourceCategory")
@Api(tags = "SysResourceCategoryController", description = "后台资源分类管理")
public class SysResourceCategoryController {
    @Autowired
    private SysResourceCategoryService resourceCategoryService;

    @ApiOperation("查询所有后台资源分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<SysResourceCategory>> listAll() {
        List<SysResourceCategory> resourceList = resourceCategoryService.listAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("添加后台资源分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody SysResourceCategory SysResourceCategory) {
        boolean success = resourceCategoryService.create(SysResourceCategory);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id,
                               @RequestBody SysResourceCategory SysResourceCategory) {
        SysResourceCategory.setId(id);
        boolean success = resourceCategoryService.updateById(SysResourceCategory);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        boolean success = resourceCategoryService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

}

