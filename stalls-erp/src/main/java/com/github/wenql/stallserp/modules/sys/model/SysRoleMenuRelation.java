package com.github.wenql.stallserp.modules.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台角色菜单关系表
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_menu_relation")
@ApiModel(value="SysRoleMenuRelation对象", description="后台角色菜单关系表")
public class SysRoleMenuRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;


}
