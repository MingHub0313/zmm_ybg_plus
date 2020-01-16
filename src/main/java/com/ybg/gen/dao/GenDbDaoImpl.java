package com.ybg.gen.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Page;
import com.ybg.gen.domain.GenDbDTO;
import com.ybg.gen.qvo.GenDbQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ybg.base.jdbc.DataBaseConstant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 代码生成数据库列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-30
 */
@Repository
public class GenDbDaoImpl extends BaseDao implements GenDbDao {

	@Autowired
	/** 注入你需要的数据库 **/
	@Qualifier(DataBaseConstant.JD_SYS)
	JdbcTemplate jdbcTemplate;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	private static String QUERY_TABLE_COLUMN = "  genDb.dbname, genDb.dburl, genDb.dbusername, genDb.dbpwd, genDb.gmt_create, genDb.gmt_modified, genDb.dbclassdriver, genDb.id";
	private static String QUERY_TABLE_NAME = "sys_gen_db  genDb";

	@Override
	public GenDbDTO save(GenDbDTO genDb) throws Exception {
		BaseMap<String, Object> createmap = new BaseMap<String, Object>();
		String id = null;
		createmap.put("dbname", genDb.getDbname());
		createmap.put("dburl", genDb.getDburl());
		createmap.put("dbusername", genDb.getDbusername());
		createmap.put("dbpwd", genDb.getDbpwd());
		createmap.put("dbclassdriver", genDb.getDbclassdriver());
		id = baseCreate(createmap, "sys_gen_db", "id");
		genDb.setId((String) id);
		return genDb;
	}

	@Override
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> WHEREmap) {
		this.baseupdate(updatemap, WHEREmap, "sys_gen_db");
	}

	@Override
	public Page list(Page page, GenDbQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(getcondition(qvo));
		page.setTotals(queryForInt(sql));
		if (page.getTotals() > 0) {
			page.setResult(
					getJdbcTemplate().query(page.getPagesql(sql), new BeanPropertyRowMapper<GenDbDTO>(GenDbDTO.class)));
		} else {
			page.setResult(new ArrayList<GenDbDTO>());
		}

		return page;
	}

	private String getcondition(GenDbQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(WHERE).append("1=1");
		sqlappen(sql, "genDb.id", qvo.getId());
		sqlappen(sql, "genDb.dbname", qvo.getDbname());
		sqlappen(sql, "genDb.dburl", qvo.getDburl());
		sqlappen(sql, "genDb.dbusername", qvo.getDbusername());
		sqlappen(sql, "genDb.dbpwd", qvo.getDbpwd());
		sqlappen(sql, "genDb.dbclassdriver", qvo.getDbclassdriver());
		return sql.toString();
	}

	@Override
	public List<GenDbDTO> list(GenDbQuery qvo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(getcondition(qvo));
		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<GenDbDTO>(GenDbDTO.class));
	}

	@Override
	public void remove(BaseMap<String, Object> wheremap) {
		baseremove(wheremap, "sys_gen_db");
	}

	@Override
	public GenDbDTO get(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(WHERE).append("1=1");
		sql.append(AND).append("id='" + id + "'");
		List<GenDbDTO> list = getJdbcTemplate().query(sql.toString(),
				new BeanPropertyRowMapper<GenDbDTO>(GenDbDTO.class));
		return QvoConditionUtil.checkList(list) ? list.get(0) : null;
	}
}
