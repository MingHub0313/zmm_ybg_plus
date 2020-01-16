package com.ybg.gen.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ybg.base.jdbc.BaseMap;
import org.springframework.web.bind.annotation.RequestBody;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.gen.domain.GenstyleDTO;
import com.ybg.gen.service.GenstyleService;

import com.ybg.gen.qvo.GenstyleQuery;
import com.ybg.base.util.ValidatorUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * 生成框架风格列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-31
 */
@Api("Genstyle管理")
@Controller
@RequestMapping("/gen/genstyle_do/")
public class GenstyleController {
	@Autowired
	private GenstyleService genstyleService;

	@ApiOperation(value = "Genstyle管理页面", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "index.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String index() {
		return "/gen/genstyle";
	}

	@ApiOperation(value = "Genstyle分页列表", notes = "JSON ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "list.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Page list(@ModelAttribute GenstyleQuery qvo, @ModelAttribute Page page, ModelMap map) throws Exception {
		qvo.setBlurred(true);

		page = genstyleService.list(page, qvo);
		return page;
	}

	@ApiOperation(value = "框架选择列表", notes = "JSON ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "selectlist.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public List<GenstyleDTO> selectlist(@ModelAttribute GenstyleQuery qvo) throws Exception {

		return genstyleService.list(qvo);

	}

	@ApiOperation(value = "更新Genstyle", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "update.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json update(@RequestBody GenstyleDTO genstyle) {
		return genstyleService.update(genstyle);
	}

	/**
	 * 框架复制，相关模版也复制
	 * 
	 * @throws Exception
	 **/
	@ApiOperation(value = "框架复制，相关模版也复制", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "copyStyle.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json copyStyle(String id) throws Exception {
		return genstyleService.copyStyle(id);
	}

	@ApiOperation(value = "根据ID删除genstyle", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "ids", value = "删除genstyle", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "remove.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json remove(@RequestParam(name = "ids", required = true) String ids2) {
		Json j = new Json();
		j.setSuccess(true);
		try {
			String[] ids = ids2.split(",");
			for (String id : ids) {
				BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
				wheremap.put("id", id);
				genstyleService.remove(wheremap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@ApiOperation(value = "创建genstyle", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "create.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json create(@RequestBody GenstyleDTO bean) {
		Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(bean);
		try {
			genstyleService.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@ApiOperation(value = "获取单个Genstyle", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "java.lang.String") })
	@RequestMapping(value = { "get.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> get(@RequestParam(name = "id", required = true) String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		GenstyleDTO bean = genstyleService.get(id);
		result.put("genstyle", bean);
		ResponseEntity<Map<String, Object>> map = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "template.do")
	public String template() {
		return "package ${package}.dao;<br/>" + "import java.util.ArrayList;<br/>" + "import java.util.List;<br/>"
				+ "import org.springframework.stereotype.Repository;<br/>" + "import com.ybg.base.jdbc.BaseDao;<br/>"
				+ "import com.ybg.base.jdbc.BaseMap;<br/>" + "import com.ybg.base.jdbc.util.QvoConditionUtil;<br/>"
				+ "import com.ybg.base.util.Page;<br/>" + "import ${package}.domain.${className}VO;<br/>"
				+ "import ${package}.qvo.${className}Query;<br/>"
				+ "import org.springframework.beans.factory.annotation.Autowired;<br/>"
				+ "import org.springframework.beans.factory.annotation.Qualifier;<br/>"
				+ "import org.springframework.jdbc.core.JdbcTemplate;<br/>"
				+ "import com.ybg.base.jdbc.DataBaseConstant;<br/>"
				+ "import org.springframework.jdbc.core.BeanPropertyRowMapper;<br/>" + "/**<br/>"
				+ " * ${comments}<br/>" + " * <br/>" + " * @author ${author}<br/>" + " * @email ${email}<br/>"
				+ " * @date ${datetime}<br/>" + " */<br/>" + "@Repository<br/>"
				+ "public class ${className}DaoImpl extends BaseDao implements ${className}Dao {<br/>" + "  <br/>"
				+ "	@Autowired<br/>" + "  /**注入你需要的数据库**/<br/>" + "	@Qualifier(DataBaseConstant.JD_REPORT)<br/>"
				+ "	JdbcTemplate jdbcTemplate;<br/>" + "  <br/>" + "	@Override<br/>"
				+ "	public JdbcTemplate getJdbcTemplate() {<br/>" + "		return jdbcTemplate;<br/>" + "	}<br/>"
				+ "<br/>"
				+ "	private static String  QUERY_TABLE_COLUMN= \"#foreach($column in $columns) #if($column.columnName != $pk.columnName)${classname}.${column.columnName},#end#end ${classname}.${pk.columnName}\";<br/>"
				+ "	private static String  QUERY_TABLE_NAME=\"${tableName}  ${classname}\";<br/>" + "	@Override<br/>"
				+ "	public ${className}VO save(${className}VO ${classname}) throws Exception {<br/>"
				+ "		BaseMap<String, Object> createmap = new BaseMap<String, Object>();<br/>"
				+ "		String id = null;		<br/>"
				+ "		#foreach($column in $columns)#if($column.columnName != $pk.columnName)createmap.put(\"${column.columnName}\", ${classname}.get${column.attrName}());<br/>"
				+ "		#end #end <br/>"
				+ "		id = baseCreate(createmap, \"${tableName}\", \"${pk.columnName}\");<br/>"
				+ "		${classname}.set${pk.attrName}((${pk.attrType}) id);<br/>" + "		return ${classname};<br/>"
				+ "	}<br/>" + "	@Override<br/>" + "	public void update(BaseMap<String, Object> updatemap,<br/>"
				+ "			BaseMap<String, Object> WHEREmap) {<br/>"
				+ "		this.baseupdate(updatemap, WHEREmap, \"${tableName}\");<br/>" + "	}<br/>" + "	@Override<br/>"
				+ "	public Page list(Page page,  ${className}Query qvo)throws Exception {<br/>"
				+ "		StringBuilder sql = new StringBuilder();<br/>"
				+ "		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM)<br/>"
				+ "				.append(QUERY_TABLE_NAME);<br/>" + "		sql.append(getcondition(qvo));<br/>"
				+ "		page.setTotals(queryForInt(sql));<br/>" + "		if (page.getTotals() > 0) {<br/>"
				+ "			page.setResult(getJdbcTemplate().query(page.getPagesql(sql),<br/>"
				+ "					 new BeanPropertyRowMapper<${className}VO>(${className}VO.class)));<br/>"
				+ "		} else {<br/>" + "			page.setResult(new ArrayList< ${className}VO>());<br/>"
				+ "		}<br/>" + "<br/>" + "		return page;<br/>" + "	}<br/>" + "<br/>"
				+ "	private String getcondition( ${className}Query qvo) throws Exception{<br/>"
				+ "		StringBuilder sql = new StringBuilder();<br/>"
				+ "		sql.append(WHERE).append(\"1=1\");		<br/>"
				+ "      #foreach($column in $columns)sqlappen(sql, \"${classname}.${column.columnName}\", qvo.get${column.attrName}());#end	<br/>"
				+ "		return sql.toString();<br/>" + "	}<br/>" + "	@Override<br/>"
				+ "	public List<${className}VO> list(${className}Query qvo) throws Exception{<br/>"
				+ "		StringBuilder sql = new StringBuilder();<br/>"
				+ "		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);<br/>"
				+ "		sql.append(getcondition(qvo));<br/>"
				+ "		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<${className}VO>(${className}VO.class));<br/>"
				+ "	}<br/>" + "	<br/>" + "	@Override<br/>"
				+ "	public void remove(BaseMap<String, Object> wheremap) {<br/>"
				+ "		 baseremove(wheremap, \"${tableName}\");		<br/>" + "	}<br/>" + "	@Override<br/>"
				+ "	public ${className}VO get(${pk.attrType} id){	<br/>"
				+ "		StringBuilder sql = new StringBuilder();<br/>"
				+ "		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);<br/>"
				+ "		sql.append(WHERE).append(\"1=1\");<br/>"
				+ "		sql.append(AND).append(\"${pk.columnName}='\"+id+\"'\");<br/>"
				+ "		List<${className}VO> list=getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<${className}VO>(${className}VO.class));		<br/>"
				+ "		return QvoConditionUtil.checkList(list)?list.get(0):null;<br/>" + "	}<br/>" + "}<br/>" + "";
	}

}
