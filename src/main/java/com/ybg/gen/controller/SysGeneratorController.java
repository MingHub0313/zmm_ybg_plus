package com.ybg.gen.controller;

import com.ybg.base.jdbc.DataBaseConstant;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.gen.qvo.GeneratorQuery;
import com.ybg.gen.service.SysGeneratorService;
import com.ybg.gen.utils.xss.XssHttpServletRequestWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午9:12:58
 */
@Api(tags = "代码生成器")
@Controller
@RequestMapping("/sys/generator_do/")
public class SysGeneratorController {

	@Autowired
	private SysGeneratorService sysGeneratorService;

	@ApiOperation(value = "代码生成器首页", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "index.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String index() {
		return "/gen/index";
	}

	@ApiOperation(value = "代码生成器列表", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNow", value = "当前页数", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "qvo", value = "查询页数", required = false, dataType = "GeneratorQuery") })
	@ResponseBody
	@RequestMapping(value = { "list.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Page list(@ModelAttribute GeneratorQuery qvo, @ModelAttribute Page page,
			@RequestParam(name = "datasource") String datasource, ModelMap map) throws Exception {
		page = sysGeneratorService.list(page, qvo, datasource);
		return page;
	}

	/**
	 * 生成代码
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "生成代码", notes = "只能页面传输，参数是tables 用英文逗号分隔", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = { "code.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public void code(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "datasource") String datasource) throws Exception {
		String[] tableNames = new String[] {};
		// 获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String tables = orgRequest.getParameter("tables");
		tableNames = tables.split(",");
		String styleid = sysGeneratorService.queryGenSetting().get("styleid");
		byte[] data = sysGeneratorService.generatorCode(tableNames, datasource, styleid);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"gencode.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}

	@ResponseBody
	@RequestMapping(value = { "updatesetting.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json updateSetting(String email, String author, String javapackage, String tablePrefix, String pathName,
			String styleid) {
		return sysGeneratorService.updateSetting(email, author, javapackage, tablePrefix, pathName, styleid);
	}

	@ResponseBody
	@RequestMapping(value = { "getsetting.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> getsetting() throws Exception {
		DataBaseConstant.setJdbcTemplate(DataBaseConstant.JD_SYS);
		Map<String, Object> result = new HashMap<String, Object>(1);
		result.putAll(sysGeneratorService.queryGenSetting());
		ResponseEntity<Map<String, Object>> map = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return map;
	}
}
