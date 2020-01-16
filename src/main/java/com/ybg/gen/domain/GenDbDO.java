package com.ybg.gen.domain;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 代码生成数据库列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-30
 */
@ApiModel("实体（数据库)")
public class GenDbDO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 **/
	@ApiModelProperty(name = "id", dataType = "java.lang.String", value = "主键", hidden = false)
	private String id;
	/** 名称 **/
	@ApiModelProperty(name = "dbname", dataType = "java.lang.String", value = "名称", hidden = false)
	private String dbname;
	/** 数据库地址 **/
	@ApiModelProperty(name = "dburl", dataType = "java.lang.String", value = "数据库地址", hidden = false)
	private String dburl;
	/** 数据库用户名称 **/
	@ApiModelProperty(name = "dbusername", dataType = "java.lang.String", value = "数据库用户名称", hidden = false)
	private String dbusername;
	/** 数据库密码 **/
	@ApiModelProperty(name = "dbpwd", dataType = "java.lang.String", value = "数据库密码", hidden = false)
	private String dbpwd;
	/** 数据库驱动 **/
	@ApiModelProperty(name = "dbclassdriver", dataType = "java.lang.String", value = "数据库驱动", hidden = false)
	private String dbclassdriver;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDbusername(String dbusername) {
		this.dbusername = dbusername;
	}

	public String getDbusername() {
		return dbusername;
	}

	public void setDbpwd(String dbpwd) {
		this.dbpwd = dbpwd;
	}

	public String getDbpwd() {
		return dbpwd;
	}

	public void setDbclassdriver(String dbclassdriver) {
		this.dbclassdriver = dbclassdriver;
	}

	public String getDbclassdriver() {
		return dbclassdriver;
	}
}
