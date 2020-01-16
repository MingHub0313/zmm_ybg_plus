package com.ybg.rbac.role.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.base.util.ValidatorUtils;
import com.ybg.rbac.RbacConstant;
import com.ybg.rbac.resources.dao.ResourcesDao;
import com.ybg.rbac.resources.domain.ResourcesDTO;
import com.ybg.rbac.resources.qvo.ResourcesQuery;
import com.ybg.rbac.role.dao.RoleDao;
import com.ybg.rbac.role.domain.RoleResDO;
import com.ybg.rbac.role.domain.BatchResRoleDTO;
import com.ybg.rbac.role.domain.RoleDTO;
import com.ybg.rbac.role.qvo.RoleQuery;

/***
 * @author https://gitee.com/YYDeament/88ybg 角色管理
 * @date 2016/10/1
 */
@Repository
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	@Autowired
	ResourcesDao resourcesDao;

	@Override
	/**
	 * 返回主键的创建
	 * 
	 * @throws Exception
	 **/
	// @CacheEvict(value = "roleCache", allEntries = true)
	public RoleDTO save(RoleDTO role) throws Exception {
		return roleDao.save(role);
	}

	/**
	 * 更新数据，条件 和 需要更新的字段都不能为空 不限个数个条件
	 * 
	 * @author Deament
	 * @param updatemap
	 *            需要更新的字段和值
	 * @param wheremap
	 *            更新中的条件字段和值
	 * @param table_name
	 *            表的名称
	 **/
	@Override
	// @CacheEvict(value = "roleCache", allEntries = true)
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap) {
		roleDao.update(updatemap, wheremap);
	}

	/** 根据id删除 **/
	@Override
	// @CacheEvict(value = "roleCache", allEntries = true)
	public void removebyid(String id) {
		BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
		BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
		wheremap.put("id", id);
		updatemap.put("isdelete", "1");
		update(updatemap, wheremap);
		// 删除 子角色

		RoleQuery qvo = new RoleQuery();
		qvo.setParentid(id);

		try {
			List<RoleDTO> roleList = roleDao.list(qvo);
			if (QvoConditionUtil.checkList(roleList)) {
				for (RoleDTO role : roleList) {
					if (QvoConditionUtil.checkString(role.getParentid())
							&& !role.getParentid().equals(RbacConstant.ROLE_ADMIN)) {
						this.removebyid(role.getId());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取单个实体信息
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "roleCache", key = "#root.method.name+#root.args[0]")
	public RoleDTO get(String id) throws Exception {
		RoleQuery qvo = new RoleQuery();
		qvo.setId(id);
		List<RoleDTO> list = roleDao.list(qvo);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "roleCache", key =
	// "#root.method.name+#root.args[0]+#root.method.name+#root.args[1]")
	public Page list(Page page, RoleQuery qvo) throws Exception {
		return roleDao.list(page, qvo);
	}

	/**
	 * 不分页查询
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "roleCache", key = "#root.method.name+#root.args[0]")
	public List<RoleDTO> list(RoleQuery qvo) throws Exception {
		return roleDao.list(qvo);
	}

	/** 角色授权 增删改都在里面了 **/
	@Override
	// @CacheEvict(value = "resroleCache", allEntries = true)
	public void saveOrUpdateRoleRes(List<RoleResDO> list) {
		roleDao.saveOrUpdateRoleRes(list);
	}

	@Override
	/**
	 * 获取所有角色，并且里面包含权限
	 * 
	 * @throws Exception
	 **/
	public Map<String, List<ResourcesDTO>> listIncludeResourceAllRole() throws Exception {
		Map<String, List<ResourcesDTO>> map = new LinkedHashMap<>();
		List<RoleDTO> list = roleDao.list(new RoleQuery());
		for (RoleDTO role : list) {
			List<ResourcesDTO> roleresourcelist = resourcesDao.getRolesByRoleId(role.getId());
			map.put(role.getRolekey(), roleresourcelist);
		}
		return map;
	}

	@Override
	public Json update(RoleDTO role) {
		Json j = new Json();
		j.setSuccess(true);
		try {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			updatemap.put("description", role.getDescription());
			updatemap.put("name", role.getName());
			updatemap.put("rolekey", role.getRolekey());
			updatemap.put("state", role.getState());
			if (!QvoConditionUtil.checkString(role.getParentid())) {
				role.setParentid(RbacConstant.DEFAULT_ROLE_PARENTID);
			}
			updatemap.put("parentid", role.getParentid());
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			wheremap.put("id", role.getId());
			roleDao.update(updatemap, wheremap);
			String roleId = role.getId();
			List<ResourcesDTO> reslist = resourcesDao.list(new ResourcesQuery());
			List<RoleResDO> list = new ArrayList<RoleResDO>();
			for (ResourcesDTO res : reslist) {
				RoleResDO rr = new RoleResDO();
				rr.setResid(res.getId());
				rr.setRoleid(roleId);
				// 禁止使用
				rr.setState(RbacConstant.RESOURCE_BAN);
				list.add(rr);
			}
			if (role.getMenuIdList() != null && role.getMenuIdList().size() > 0) {
				for (String resid : role.getMenuIdList()) {
					for (RoleResDO rr : list) {
						if (rr.getResid().equals(resid)) {
							rr.setState(RbacConstant.RESOURCE_ALLOW);
						}
					}
				}
			}
			roleDao.saveOrUpdateRoleRes(list);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;

	}

	@Override
	public Json createRoleWithResources(RoleDTO bean) {
		Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(bean);
		try {
			roleDao.save(bean);
			String roleId = bean.getId();
			List<ResourcesDTO> reslist = resourcesDao.list(new ResourcesQuery());
			List<RoleResDO> list = new ArrayList<RoleResDO>();
			for (ResourcesDTO res : reslist) {
				RoleResDO rr = new RoleResDO();
				rr.setResid(res.getId());
				rr.setRoleid(roleId);
				// 禁止使用
				rr.setState(RbacConstant.RESOURCE_BAN);
				list.add(rr);
			}
			if (bean.getMenuIdList() != null && bean.getMenuIdList().size() > 0) {
				for (String resid : bean.getMenuIdList()) {
					for (RoleResDO rr : list) {
						if (rr.getResid().equals(resid)) {
							rr.setState(RbacConstant.RESOURCE_ALLOW);
						}
					}
				}
			}
			roleDao.saveOrUpdateRoleRes(list);
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			j.setMsg("操作失败,已存在该记录");
			return j;
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;

	}

	@Override
	public Json batchauthorize(BatchResRoleDTO bean) {
		Json j = new Json();
		try {
			for (String roleId : bean.getRoleIdList()) {
				List<ResourcesDTO> reslist = resourcesDao.list(new ResourcesQuery());
				List<RoleResDO> list = new ArrayList<RoleResDO>();
				for (ResourcesDTO res : reslist) {
					RoleResDO rr = new RoleResDO();
					rr.setResid(res.getId());
					rr.setRoleid(roleId);
					// 禁止使用
					rr.setState(RbacConstant.RESOURCE_BAN);
					list.add(rr);
				}
				if (bean.getMenuIdList() != null && bean.getMenuIdList().size() > 0) {
					for (String resid : bean.getMenuIdList()) {
						for (RoleResDO rr : list) {
							if (rr.getResid().equals(resid)) {
								rr.setState(RbacConstant.RESOURCE_ALLOW);
							}
						}
					}
				}
				roleDao.saveOrUpdateRoleRes(list);
			}
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			j.setMsg("操作失败,已存在该记录");
			return j;
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}

	@Override
	public RoleDTO getRoleInfo(String id) throws Exception {
		RoleDTO role = roleDao.getById(id);
		List<ResourcesDTO> menusvo = resourcesDao.getRolesByRoleId(id);
		List<String> list = new ArrayList<String>();
		for (ResourcesDTO r : menusvo) {
			list.add(r.getId());
		}
		return role;
	}
}
