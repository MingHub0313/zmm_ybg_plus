package com.ybg.gen.service;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ybg.gen.dao.GenDbDao;

import com.ybg.gen.domain.GenDbDTO;
import com.ybg.gen.qvo.GenDbQuery;
import java.util.List;

import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.base.util.ValidatorUtils;

/**
 * 代码生成数据库列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-30
 */

@Repository
public class GenDbServiceImpl implements GenDbService {
	@Autowired
	private GenDbDao genDbDao;

	@Override
	public GenDbDTO save(GenDbDTO bean) throws Exception {
		return genDbDao.save(bean);

	}

	@Override
	public Json update(GenDbDTO genDb) {
		Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(genDb);
		try {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			updatemap.put("dbname", genDb.getDbname());
			updatemap.put("dburl", genDb.getDburl());
			updatemap.put("dbusername", genDb.getDbusername());
			updatemap.put("dbpwd", genDb.getDbpwd());
			updatemap.put("dbclassdriver", genDb.getDbclassdriver());
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			wheremap.put("id", genDb.getId());
			genDbDao.update(updatemap, wheremap);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@Override
	public Page list(Page page, GenDbQuery qvo) throws Exception {
		return genDbDao.list(page, qvo);
	}

	@Override
	public List<GenDbDTO> list(GenDbQuery qvo) throws Exception {
		return genDbDao.list(qvo);
	}

	@Override
	public void remove(BaseMap<String, Object> wheremap) {
		genDbDao.remove(wheremap);
	}

	@Override
	public GenDbDTO get(String id) {
		return genDbDao.get(id);
	}
}
