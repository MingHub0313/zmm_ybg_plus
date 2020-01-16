package com.ybg.weixin.menu.dao;
import java.util.List;
import com.ybg.base.util.BeanToMapUtil;
import com.ybg.weixin.menu.domain.WeixinButtonDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.weixin.menu.WeixinButtonConstant;
import com.ybg.weixin.menu.domain.WeixinButtonDTO;

/*** @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1 */
@Repository
public class WeixinButtonDaoImpl extends BaseDao implements WeixinButtonDao {
	
	private static String	QUERY_TABLE_COLUMN	= "weixinMenu.menuorder,weixinMenu.buttonorder, weixinMenu.ifsub,weixinMenu.type,weixinMenu.name,weixinMenu.key,weixinMenu.url,weixinMenu.media_id,weixinMenu.appid,weixinMenu.pagepath,weixinMenu.parentid,weixinMenu.gmt_create,weixinMenu.gmt_modified, id";
	private static String	QUERY_TABLE_NAME	= "weixin_menu  weixinMenu";
	@Autowired
	JdbcTemplate			jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	@Override
	public void create(WeixinButtonDTO weixinMenu) throws Exception {
		BaseMap<String, Object> createmap = BeanToMapUtil.copyBeanToMap(weixinMenu, new WeixinButtonDO(), "id");
		baseCreate(createmap, "weixin_menu", "id");
	}
	
	@Override
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap) {
		baseupdate(updatemap, wheremap, "weixin_menu");
	}
	
	@Override
	public void remove(BaseMap<String, Object> conditionmap) {
		baseremove(conditionmap, "weixin_menu");
	}
	
	@Override
	public List<WeixinButtonDTO> list() {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<WeixinButtonDTO>(WeixinButtonDTO.class));
	}
	
	@Override
	public WeixinButtonDTO get(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(WHERE).append("1=1");
		sql.append(AND).append("id='" + id + "'");
		List<WeixinButtonDTO> list = getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<WeixinButtonDTO>(WeixinButtonDTO.class));
		return QvoConditionUtil.checkList(list) ? list.get(0) : null;
	}
	
	@Override
	public List<WeixinButtonDTO> menulist() {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(WHERE).append("1=1");
		sql.append(AND).append(" weixinMenu.parentid ").append(IS).append(NULL);
		// sqlappen(sql, "weixinMenu.ifsub", WeixinButtonConstant.IFSUBNO);
		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<WeixinButtonDTO>(WeixinButtonDTO.class));
	}
	
	@Override
	public List<WeixinButtonDTO> buttonlist(String parentid) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
		sql.append(WHERE).append("1=1");
		sql.append(AND).append(" weixinMenu.parentid ").append(IS).append(NOT).append(NULL);
		sqlappen(sql, "parentid", parentid);
		sqlappen(sql, "weixinMenu.ifsub", WeixinButtonConstant.IFSUBYES);
		return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<WeixinButtonDTO>(WeixinButtonDTO.class));
	}
}
