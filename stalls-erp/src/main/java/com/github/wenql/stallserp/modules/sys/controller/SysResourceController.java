package com.github.wenql.stallserp.modules.sys.controller;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wenql.stallserp.common.api.CommonPage;
import com.github.wenql.stallserp.common.api.CommonResult;
import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.service.SysResourceService;
import com.github.wenql.stallserp.security.component.DynamicSecurityMetadataSource;

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
 * 后台资源表 前端控制器
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/Resource")
@Api(tags = "SysResourceController", description = "后台资源管理")
public class SysResourceController {

    @Autowired
    private SysResourceService resourceService;
    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @ApiOperation("添加后台资源")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody SysResource SysResource) {
        boolean success = resourceService.create(SysResource);
        dynamicSecurityMetadataSource.clearDataSource();
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改后台资源")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id,
                               @RequestBody SysResource SysResource) {
        boolean success = resourceService.update(id, SysResource);
        dynamicSecurityMetadataSource.clearDataSource();
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID获取资源详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<SysResource> getItem(@PathVariable Long id) {
        SysResource SysResource = resourceService.getById(id);
        return CommonResult.success(SysResource);
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        boolean success = resourceService.delete(id);
        dynamicSecurityMetadataSource.clearDataSource();
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页模糊查询后台资源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<SysResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysResource> resourceList = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查询所有后台资源")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<SysResource>> listAll() {
        List<SysResource> resourceList = resourceService.list();
        return CommonResult.success(resourceList);
    }

}

