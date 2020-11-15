package com.github.wenql.stallserp.modules.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import com.github.wenql.stallserp.modules.sys.model.SysMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 */
@Getter
@Setter
public class SysMenuNode extends SysMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<SysMenuNode> children;
}
