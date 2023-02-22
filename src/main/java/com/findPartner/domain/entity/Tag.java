package com.findPartner.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签表
 *
 * @TableName tag
 */
@TableName(value = "tag")
@Data
public class Tag implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 上传标签的用户的 id
     */
    private Integer userId;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 标签 id
     */
    private Integer tagId;
    /**
     * 父标签 id
     */
    private Integer parentId;
    /**
     * 是否为父标签 （0-不是父标签 1-父标签）
     */
    private Integer isParent;
    /**
     * 是否删除(逻辑删除)
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}