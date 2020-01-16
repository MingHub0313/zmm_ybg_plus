package com.ybg.gen.controller;

import java.util.HashMap;
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
import com.ybg.gen.domain.GenTempDTO;
import com.ybg.gen.service.GenTempService;
import com.ybg.gen.qvo.GenTempQuery;
import com.ybg.base.util.ValidatorUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * 生成模板管理
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-02-05
 */
@Api("生成模板管理")
@Controller
@RequestMapping("/sys/gentemp_do/")
public class GenTempController {

	@Autowired
	private GenTempService genTempService;

	@ApiOperation(value = "GenTemp管理页面", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "index.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String index() {
		return "/gen/gentemp";
	}

	@ApiOperation(value = "GenTemp分页列表", notes = "JSON ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "list.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Page list(@ModelAttribute GenTempQuery qvo, @ModelAttribute Page page, ModelMap map) throws Exception {
		qvo.setBlurred(true);
		page = genTempService.list(page, qvo);
		return page;
	}

	@ApiOperation(value = "更新GenTemp", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "update.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json update(@RequestBody GenTempDTO genTemp) {
		return genTempService.update(genTemp);
	}

	@ApiOperation(value = "根据ID删除genTemp", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "ids", value = "删除genTemp", required = true, dataType = "java.lang.String")
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
				genTempService.remove(wheremap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@ApiOperation(value = "创建genTemp", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "create.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json create(@RequestBody GenTempDTO bean) {
		Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(bean);
		try {
			genTempService.save(bean);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@ApiOperation(value = "获取单个GenTemp", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "java.lang.String") })
	@RequestMapping(value = { "get.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> get(@RequestParam(name = "id", required = true) String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		GenTempDTO bean = genTempService.get(id);
		result.put("genTemp", bean);
		ResponseEntity<Map<String, Object>> map = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return map;
	}
}
