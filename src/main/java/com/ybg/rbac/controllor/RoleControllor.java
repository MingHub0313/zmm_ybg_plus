package com.ybg.rbac.controllor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.rbac.resources.service.ResourcesService;
import com.ybg.rbac.role.domain.BatchResRoleDTO;
import com.ybg.rbac.role.domain.RoleDTO;
import com.ybg.rbac.role.qvo.RoleQuery;
import com.ybg.rbac.role.service.RoleService;

/**
 * @author Deament
 * 
 * @date 2016/9/31
 ***/
@Api(tags = "角色管理")
@Controller
@RequestMapping("/role/role_do/")
public class RoleControllor {

	@Autowired
	RoleService roleService;
	@Autowired
	ResourcesService resourcesService;

	@ApiOperation(value = "角色管理页面", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "index.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String index() {
		return "/system/role/index";
	}

	@ApiOperation(value = "角色分页列表", notes = "JSON ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "list.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Page list(@ModelAttribute RoleQuery qvo, @ModelAttribute Page page) throws Exception {
		qvo.setBlurred(true);
		page = roleService.list(page, qvo);
		return page;
	}

	/**
	 * 角色下拉列表
	 * 
	 * @throws Exception
	 **/
	@ResponseBody
	@RequestMapping(value = { "select.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> select() throws Exception {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("roleselect", roleService.list(new RoleQuery()));
		return map;
	}

	/**
	 * 角色下拉列表
	 * 
	 * @throws Exception
	 **/
	@ResponseBody
	@RequestMapping(value = { "selecttreetable.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public List<RoleDTO> selecttreetable() throws Exception {
		return roleService.list(new RoleQuery());
	}

	@ApiOperation(value = "更新角色", notes = "只更新描述，名称，系统唯一标识,状态 ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "update.do" }, method = { RequestMethod.POST })
	public Json update(@RequestBody RoleDTO role) {
		return roleService.update(role);
	}

	@ApiOperation(value = "根据ID删除角色", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "ids", value = "删除角色的ID", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "remove.do" }, method = { RequestMethod.POST })
	public Json remove(@RequestParam(name = "ids", required = true) String ids2) {
		Json j = new Json();
		j.setSuccess(true);
		try {
			String[] ids = ids2.split(",");
			for (String id : ids) {
				roleService.removebyid(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@ApiOperation(value = "创建角色", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "create.do" }, method = { RequestMethod.POST })
	public Json create(@RequestBody RoleDTO bean) {
		return roleService.createRoleWithResources(bean);
	}

	@ResponseBody
	@RequestMapping(value = { "batchauthorize.do" }, method = { RequestMethod.POST })
	public Json batchauthorize(@RequestBody BatchResRoleDTO bean) {
		return roleService.batchauthorize(bean);
	}

	@ApiOperation(value = "获取单个ROLE", notes = " ", produces = MediaType.TEXT_HTML_VALUE)
	@ApiImplicitParam(name = "id", value = "角色的ID", required = true, dataType = "java.lang.String")
	@ResponseBody
	@RequestMapping(value = { "get.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> get(@RequestParam(name = "id", required = true) String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>(100);
		RoleDTO role = roleService.getRoleInfo(id);
		result.put("role", role);
		ResponseEntity<Map<String, Object>> bean = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		return bean;
	}
}
