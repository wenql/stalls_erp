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
 * 后台用户和角色关系表
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_admin_role_relation")
@ApiModel(value="SysAdminRoleRelation对象", description="后台用户和角色关系表")
public class SysAdminRoleRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long adminId;

    private Long roleId;


}
