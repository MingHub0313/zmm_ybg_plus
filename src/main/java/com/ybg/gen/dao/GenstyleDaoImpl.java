package com.ybg.gen.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Page;
import com.ybg.gen.domain.GenstyleDTO;
import com.ybg.gen.qvo.GenstyleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ybg.base.jdbc.DataBaseConstant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 生成框架风格列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-31
 */
@Repository
public class GenstyleDaoImpl extends BaseDao implements GenstyleDao {

	@Autowired
	/** 注入你需要的数据库 **/
	@Qualifier(DataBaseConstant.JD_SYS)
	JdbcTemplate jdbcTemplate;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	private static String QUERY_TABLE_COLUMN = "  genstyle.name, genstyle.gmt_create, genstyle.gmt_modified, genstyle.description, genstyle.id";
	private static String QUERY_TABLE_NAME = "sys_genstyle  genstyle";

	@Override
	public GenstyleDTO save(GenstyleDTO genstyle) throws Exception {
		BaseMap<String, Object> createmap = new BaseMap<String, Object>();
		String id = null;
		createmap.put("name", genstyle.getName());
		
		createmap.put("description", genstyle.getDescription());
		id = baseCreate(createmap, "sys_genstyle", "id");
		genstyle.setId((String) id);
		return genstyle;
	}

	@Override
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> WHEREmap) {
		this.baseupdate(updatemap, WHEREmap, "sys_genstyle");
	}

	@Override
	public Page list(Page page, GenstyleQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(getcondition(qvo));
		page.setTotals(queryForInt(sql));
		if (page.getTotals() > 0) {
			page.setResult(getJdbcTemplate().query(page.getPagesql(sql),
					new BeanPropertyRowMapper<GenstyleDTO>(GenstyleDTO.class)));
		} else {
			page.setResult(new ArrayList<GenstyleDTO>());
		}

		return page;
	}

	private String getcondition(GenstyleQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(WHERE).append("1=1");
		sqlappen(sql, "genstyle.id", qvo.getId());
		sqlappen(sql, "genstyle.name", qvo.getName());
		
		sqlappen(sql, "genstyle.description", qvo.getDescription());
		return sql.toString();
	}

	@Override
	public List<GenstyleDTO> list(GenstyleQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(getcondition(qvo));
		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<GenstyleDTO>(GenstyleDTO.class));
	}

	@Override
	public void remove(BaseMap<String, Object> wheremap) {
		baseremove(wheremap, "sys_genstyle");
	}

	@Override
	public GenstyleDTO get(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(WHERE).append("1=1");
		sql.append(AND).append("id='" + id + "'");
		List<GenstyleDTO> list = getJdbcTemplate().query(sql.toString(),
				new BeanPropertyRowMapper<GenstyleDTO>(GenstyleDTO.class));
		return QvoConditionUtil.checkList(list) ? list.get(0) : null;
	}
}
