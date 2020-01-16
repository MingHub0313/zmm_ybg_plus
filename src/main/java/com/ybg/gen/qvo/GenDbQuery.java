package com.ybg.gen.qvo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.ybg.base.jdbc.BaseQueryAble;
import com.ybg.gen.domain.GenDbDO;


/**
 * 代码生成数据库列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-30
 */
@ApiModel(value = "角色查询条件", parent = GenDbDO.class)
public class GenDbQuery extends GenDbDO implements BaseQueryAble {
	
	private static final long	serialVersionUID	= -1516990738029741157L;
	/** 是否模糊查询 **/
	@ApiModelProperty(name = "blurred", dataType = "java.lang.Boolean", value = "是否模糊查询", hidden = true)
	private boolean				blurred;
	
	public boolean isBlurred() {
		return blurred;
	}
	
	public void setBlurred(boolean blurred) {
		this.blurred = blurred;
	}
	
	
}