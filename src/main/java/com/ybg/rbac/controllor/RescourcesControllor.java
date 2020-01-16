package com.ybg.rbac.controllor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ybg.base.util.Json;
import com.ybg.rbac.resources.domain.ResourcesDTO;
import com.ybg.rbac.resources.qvo.ResourcesQuery;
import com.ybg.rbac.resources.service.ResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author Deament
 * 
 * @date 2016/9/31
 ***/
@Api(tags = "用户访问资源管理接口")
@Controller
@RequestMapping("/res/res_do/")
public class RescourcesControllor {

	@Autowired
	ResourcesService resourcesService;

	@ApiOperation(value = "资源页面", notes = "需要授权才可以访问的页面", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "index.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap map) {
		return "/system/resources/index";
	}

	@ApiOperation(value = "资源页面数据列表", notes = "需要授权才可以访问的页面", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "list.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public List<ResourcesDTO> list(@ModelAttribute ResourcesQuery qvo) throws Exception {
		return resourcesService.list(qvo);
	}

	@ApiOperation(value = "选择列表", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "select.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> select(@ModelAttribute ResourcesQuery qvo) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>(100);
		result.put("menuList", resourcesService.list(qvo));
		ResponseEntity<Map<String, Object>> map = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return map;
	}

	@ApiOperation(value = "创建授权资源", notes = "需要授权才可以访问的页面", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "create.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json create(@RequestBody ResourcesDTO res) {

		return resourcesService.save(res);
	}

	@ApiOperation(value = "更新授权资源", notes = "需要授权才可以访问的页面", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "update.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json update(@RequestBody ResourcesDTO menu) {
		return resourcesService.update(menu);
	}

	@ApiOperation(value = "删除授权资源", notes = "逻辑删除", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "删除资源的ID", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "remove.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json remove(@RequestParam(name = "id", required = true) String id) {
		Json j = new Json();
		j.setSuccess(true);
		try {
			resourcesService.removebyid(id);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	/** 树 **/
	@ApiOperation(value = "授权资源树结构", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "reslists.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> reslists(@ModelAttribute ResourcesQuery qvo) throws Exception {
		Map<String, Object> maps = new HashMap<String, Object>(100);
		List<ResourcesDTO> list = resourcesService.list(qvo);
		maps.put("menuList", list);
		return maps;
	}

	@ApiOperation(value = "加载角色所属授权资源", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "roleid", value = "角色ID", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "findRes.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public List<ResourcesDTO> findUserRes(@RequestParam(name = "roleid", required = true) String roleid)
			throws Exception {
		return resourcesService.getRolesByRoleId(roleid);
	}

	@ApiOperation(value = "获取单个授权资源", notes = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "授权资源ID", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "get.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> get(@RequestParam(name = "id", required = true) String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>(100);
		ResourcesDTO vo = resourcesService.get(id);
		result.put("menu", vo);
		ResponseEntity<Map<String, Object>> bean = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return bean;
	}
}
