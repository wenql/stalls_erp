package com.github.wenql.stallserp.modules.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_tenant")
@ApiModel(value="SysTenant对象", description="租户表")
public class SysTenant implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "租户启用状态：0->禁用；1->启用")
    private Integer status;


}
