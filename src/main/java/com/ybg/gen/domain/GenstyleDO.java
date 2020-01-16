package com.ybg.gen.domain;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生成框架风格列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-31
 */
@ApiModel("实体（数据库)")
public class GenstyleDO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 **/
	@ApiModelProperty(name = "id", dataType = "java.lang.String", value = "主键", hidden = false)
	private String id;
	/** 框架名称 **/
	@ApiModelProperty(name = "name", dataType = "java.lang.String", value = "框架名称", hidden = false)
	private String name;

	/** 描述 **/
	@ApiModelProperty(name = "description", dataType = "java.lang.String", value = "描述", hidden = false)
	private String description;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
