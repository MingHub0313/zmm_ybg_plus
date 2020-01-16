package com.ybg.gen.service;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.ybg.gen.dao.GenTempDao;
import com.ybg.gen.dao.GenstyleDao;
import com.ybg.gen.domain.GenTempDTO;
import com.ybg.gen.domain.GenstyleDTO;
import com.ybg.gen.qvo.GenTempQuery;
import com.ybg.gen.qvo.GenstyleQuery;
import java.util.List;

import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.base.util.ValidatorUtils;

/**
 * 生成框架风格列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-31
 */

@Repository
public class GenstyleServiceImpl implements GenstyleService {
	@Autowired
	private GenstyleDao genstyleDao;
	@Autowired
	GenTempDao genTempDao;

	@Override
	public GenstyleDTO save(GenstyleDTO bean) throws Exception {
		return genstyleDao.save(bean);

	}

	@Override
	public Json update(GenstyleDTO genstyle) {
		Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(genstyle);
		try {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			updatemap.put("name", genstyle.getName());
			updatemap.put("description", genstyle.getDescription());
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			wheremap.put("id", genstyle.getId());
			genstyleDao.update(updatemap, wheremap);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;

	}

	@Override
	public Page list(Page page, GenstyleQuery qvo) throws Exception {
		return genstyleDao.list(page, qvo);
	}

	@Override
	public List<GenstyleDTO> list(GenstyleQuery qvo) throws Exception {
		return genstyleDao.list(qvo);
	}

	@Override
	public void remove(BaseMap<String, Object> wheremap) {
		genstyleDao.remove(wheremap);
	}

	@Override
	public GenstyleDTO get(String id) {
		return genstyleDao.get(id);
	}

	@Override
	public Json copyStyle(String id) throws Exception {
		Json j = new Json();
		try {
			GenstyleDTO source = get(id);
			source.setName("复制的" + source.getName());
			save(source);
			GenTempQuery qvo = new GenTempQuery();
			qvo.setStyleid(id);
			List<GenTempDTO> sourcetemps = genTempDao.list(qvo);
			for (GenTempDTO st : sourcetemps) {
				// 更变ID为新的ID
				st.setStyleid(source.getId());
				genTempDao.save(st);

			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			j.setSuccess(false);
			return j;
		}
		j.setMsg("操作成功");
		j.setSuccess(true);
		return j;
	}
}
