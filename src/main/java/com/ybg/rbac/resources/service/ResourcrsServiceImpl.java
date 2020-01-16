package com.ybg.rbac.resources.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

/***
 * @author https://gitee.com/YYDeament/88ybg
 * @date 2016/10/1
 */
@Repository
public class ResourcrsServiceImpl implements ResourcesService {

	@Autowired
	ResourcesDao resourcesDao;

	/** 返回主键的创建 **/
	@Override
	// @Caching(evict = { @CacheEvict(value = "resourcesCache", allEntries = true),
	// @CacheEvict(value = "resroleCache", allEntries = true) })
	public Json save(ResourcesDTO res) {
		Json j = new Json();
		j.setSuccess(true);
		if (res.getType().equals(RbacConstant.RESOURCE_MENU)) {
			res.setReskey(res.getName());
			res.setParentid(RbacConstant.RESOURCE_DEFAULT_PARENTID);
			res.setResurl(res.getName());
		} else {
			res.setReskey(res.getResurl());
		}
		ValidatorUtils.validateEntity(res);
		try {
			resourcesDao.save(res);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;

		// return resourcesDao.save(bean);
	}

	/** 根据ID删除 **/
	@Override
	// @Caching(evict = { @CacheEvict(value = "resourcesCache", allEntries = true),
	// @CacheEvict(value = "resroleCache", allEntries = true) })
	public void removebyid(String id) {

		BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
		BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
		updatemap.put("isdelete", 1);
		wheremap.put("id", id);
		update(updatemap, wheremap);
		if (QvoConditionUtil.checkString(id) && !id.equals(RbacConstant.DEFAULT_RESOURCES_PARENTID)) {
			ResourcesQuery qvo = new ResourcesQuery();
			qvo.setParentid(id);
			try {
				List<ResourcesDTO> list = this.list(qvo);
				for (ResourcesDTO l : list) {
					if (QvoConditionUtil.checkList(list)) {
						this.removebyid(l.getId());
						// 删除子目录
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
	// @Caching(evict = { @CacheEvict(value = "resourcesCache", allEntries = true),
	// @CacheEvict(value = "resroleCache", allEntries = true) })
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap) {
		resourcesDao.update(updatemap, wheremap);
	}

	/**
	 * 获取单个实体信息
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "resourcesCache", key = "#root.method.name+#root.args[0]")
	public ResourcesDTO get(String id) throws Exception {
		ResourcesQuery qvo = new ResourcesQuery();
		qvo.setId(id);
		List<ResourcesDTO> list = resourcesDao.list(qvo);

		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "resourcesCache", key =
	// "#root.method.name+#root.args[0]+#root.method.name+#root.args[1]")
	public Page list(Page page, ResourcesQuery qvo) throws Exception {
		return resourcesDao.list(page, qvo);
	}

	/**
	 * 不分页查询
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "resourcesCache", key = "#root.method.name+#root.args[0]")
	public List<ResourcesDTO> list(ResourcesQuery qvo) throws Exception {
		return resourcesDao.list(qvo);
	}

	/**
	 * 角色 权限集合
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "resroleCache", key = "#root.method.name+#root.args[0]")
	public List<ResourcesDTO> getRolesByRoleId(String roleid) throws Exception {
		return resourcesDao.getRolesByRoleId(roleid);
	}

	@Override
	// @Cacheable(value = "resroleCache", key = "#root.method.name+#root.args[0]")
	public List<ResourcesDTO> getRolesByRoleIds(List<String> roleids) throws Exception {
		List<ResourcesDTO> list = new ArrayList<>();
		if (QvoConditionUtil.checkList(roleids)) {
			for (String roleid : roleids) {
				list.addAll(resourcesDao.getRolesByRoleId(roleid));
			}
		}
		return list;
	}

	/**
	 * 授权的按钮操作
	 * 
	 * @throws Exception
	 **/
	@Override
	//	@Cacheable(value = "resroleCache", key = "#root.method.name+#root.args[0]+#root.method.name+#root.args[1]")
	public List<ResourcesDTO> getOperatorButton(String roleid, String parentid) throws Exception {
		return resourcesDao.getOperatorButton(roleid, parentid);
	}

	@Override
	// @Cacheable(value = "resroleCache", key = "#root.method.name")
	public List<ResourcesDTO> getRolesByRoleIdHaveNull() throws Exception {
		return resourcesDao.getRolesByRoleIdHaveNull();
	}

	@Override
	public Json update(ResourcesDTO menu) {

		Json j = new Json();
		j.setSuccess(true);
		if (menu.getType().equals(RbacConstant.RESOURCE_MENU)) {
			menu.setReskey(menu.getName());
			menu.setParentid(RbacConstant.RESOURCE_DEFAULT_PARENTID);
			menu.setResurl(menu.getName());
		} else {
			menu.setReskey(menu.getResurl());
		}
		ValidatorUtils.validateEntity(menu);
		try {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			updatemap.put("description", menu.getDescription());
			updatemap.put("icon", menu.getIcon());
			updatemap.put("ishide", menu.getIshide());
			updatemap.put("level", menu.getLevel());
			updatemap.put("name", menu.getName());
			if (!QvoConditionUtil.checkString(menu.getParentid())) {
				menu.setParentid(RbacConstant.DEFAULT_RESOURCES_PARENTID);
			}
			updatemap.put("parentid", menu.getParentid());
			updatemap.put("reskey", menu.getReskey());
			updatemap.put("resurl", menu.getResurl());
			updatemap.put("type", menu.getType());
			updatemap.put("colorid", menu.getColorid());
			wheremap.put("id", menu.getId());
			resourcesDao.update(updatemap, wheremap);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}
}
