package com.ybg.gen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.BaseQueryAble;

import com.ybg.base.util.Page;
import com.ybg.gen.domain.GenDbDTO;
import com.ybg.gen.entity.TableEntity;
import com.ybg.gen.qvo.GeneratorQuery;

/***
 * @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1
 */
@Repository
public class SysGeneratorDaoImpl extends BaseDao implements SysGeneratorDao {

	/** 默认使用sys 如果修改数据库 @Qualifier 修改 @Qualifier(DataBaseConstant.JD_EDU) */
	@Autowired
	JdbcTemplate jdbcTemplate;
	// @Autowired
	// @Qualifier(value = DataBaseConstant.JD_QUARTZ)
	// JdbcTemplate quartzjdbcTemplate;
	// @Autowired
	// @Qualifier(value = DataBaseConstant.JD_REPORT)
	// JdbcTemplate reportjdbcTemplate;

	@Autowired
	GenDbDao genDbDao;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		// 切换数据源
		// String dbname = DataBaseConstant.getJdbcTemplate();
		// if (dbname == null) {
		// dbname = DataBaseConstant.JD_SYS;
		// }
		// switch (dbname) {
		// case DataBaseConstant.JD_SYS:
		// return jdbcTemplate;
		// case DataBaseConstant.JD_QUARTZ:
		// return quartzjdbcTemplate;
		// case DataBaseConstant.JD_REPORT:
		// return reportjdbcTemplate;
		// default:
		// return jdbcTemplate;
		// }
		return jdbcTemplate;
	}

	/** 改成远程的JDBC访问方式 ****/
	@Override
	public Page list(Page page, GeneratorQuery qvo, String databaseid) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables ");
		sql.append(" 	where table_schema = (select database())");
		sqlappen(sql, "table_name", qvo.getTablename(), new BaseQueryAble() {

			@Override
			public boolean isBlurred() {
				return true;
			}
		});
		// page.setTotals(queryForInt(sql));
		// if (page.getTotals() > 0) {
		// List<TableEntity> list = getJdbcTemplate().query(page.getPagesql(sql), new
		// BeanPropertyRowMapper<TableEntity>(TableEntity.class));
		// page.setResult(list);
		// }
		// else {
		// page.setResult(new ArrayList<TableEntity>());
		// }
		GenDbDTO bean = genDbDao.get(databaseid);

		Connection conn = null;

		Class.forName(bean.getDbclassdriver()).newInstance();
		conn = java.sql.DriverManager.getConnection(bean.getDburl(), bean.getDbusername(), bean.getDbpwd());
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(page.getPagesql(sql));
			ResultSet rs = pstmt.executeQuery();
			// int col = rs.getMetaData().getColumnCount();
			List<TableEntity> tablelist = new ArrayList<>();
			while (rs.next()) {
				TableEntity tb = new TableEntity();
				// tb.setClassname(rs.getString("classname"));
				tb.setTableName(rs.getString("tableName"));
				tb.setComments(rs.getString("tableComment"));
				tablelist.add(tb);

			}
			page.setResult(tablelist);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (conn != null) {
			try {

				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		conn = null;
		pstmt = null;
		Class.forName(bean.getDbclassdriver()).newInstance();
		conn = java.sql.DriverManager.getConnection(bean.getDburl(), bean.getDbusername(), bean.getDbpwd());
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(page.getCountsql(sql));
			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			pstmt.close();
			page.setTotals(count);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return page;
	}

	@Override
	public Map<String, String> queryTable(String tableName, String databaseId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables ");
		sql.append(" where table_schema = (select database()) and table_name = '" + tableName + "'");
		Map<String, String> map = new LinkedHashMap<String, String>();
		// getJdbcTemplate().query(sql.toString(), new RowMapper<Object>() {
		//
		// @Override
		// public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		// map.put("tableName", rs.getString("tableName"));
		// map.put("engine", rs.getString("engine"));
		// map.put("tableComment", rs.getString("tableComment"));
		// map.put("createTime", rs.getString("createTime"));
		// return null;
		// }
		// });
		GenDbDTO bean = genDbDao.get(databaseId);
		Connection conn = null;

		Class.forName(bean.getDbclassdriver()).newInstance();
		conn = java.sql.DriverManager.getConnection(bean.getDburl(), bean.getDbusername(), bean.getDbpwd());
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			// int col = rs.getMetaData().getColumnCount();
			// List<TableEntity> tablelist = new ArrayList<>();
			while (rs.next()) {
				map.put("tableName", rs.getString("tableName"));
				map.put("engine", rs.getString("engine"));
				map.put("tableComment", rs.getString("tableComment"));
				map.put("createTime", rs.getString("createTime"));

			}

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (conn != null) {
			try {

				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	@Override
	public List<Map<String, String>> queryColumns(String tableName, String databaseId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns");
		sql.append(" where table_name = '" + tableName
				+ "' and table_schema = (select database()) order by ordinal_position");
		// return getJdbcTemplate().query(sql.toString(), new RowMapper<Map<String,
		// String>>() {
		//
		// @Override
		// public Map<String, String> mapRow(ResultSet rs, int index) throws
		// SQLException {
		// Map<String, String> map = new LinkedHashMap<String, String>();
		// map.put("columnName", rs.getString("columnName"));
		// map.put("dataType", rs.getString("dataType"));
		// map.put("columnComment", rs.getString("columnComment"));
		// map.put("columnKey", rs.getString("columnKey"));
		// map.put("extra", rs.getString("extra"));
		// return map;
		// }
		// });
		List<Map<String, String>> list = new ArrayList<>();
		GenDbDTO bean = genDbDao.get(databaseId);
		Connection conn = null;

		Class.forName(bean.getDbclassdriver()).newInstance();
		conn = java.sql.DriverManager.getConnection(bean.getDburl(), bean.getDbusername(), bean.getDbpwd());
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			// List<TableEntity> tablelist = new ArrayList<>();
			while (rs.next()) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("columnName", rs.getString("columnName"));
				map.put("dataType", rs.getString("dataType"));
				map.put("columnComment", rs.getString("columnComment"));
				map.put("columnKey", rs.getString("columnKey"));
				map.put("extra", rs.getString("extra"));
				list.add(map);
			}

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (conn != null) {
			try {

				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public Map<String, String> queryGenSetting() {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append("`key`,`value` ").append(FROM).append(" sys_gen gen");
		Map<String, String> map = new LinkedHashMap<String, String>();
		jdbcTemplate.query(sql.toString(), new RowMapper<Object>() {

			@Override
			public Map<String, String> mapRow(ResultSet rs, int index) throws SQLException {
				map.put(rs.getString("key"), rs.getString("value"));
				return null;
			}
		});
		return map;
	}

	@Override
	public void updateGenSetting(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap) {
		baseupdate(updatemap, wheremap, "sys_gen");
	}
}
