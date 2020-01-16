package com.ybg.rbac.resources.dao;
import java.util.ArrayList;
import java.util.List;
import com.ybg.base.util.BeanToMapUtil;
import com.ybg.rbac.resources.domain.ResourcesDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Page;
import com.ybg.rbac.resources.domain.ResourcesDTO;
import com.ybg.rbac.resources.qvo.ResourcesQuery;

/***
 * @author https://gitee.com/YYDeament/88ybg
 * 
 * 
 * @date 2016/10/1
 */
@Repository
public class ResourcesDaoImpl extends BaseDao implements ResourcesDao {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
    
    private static String QUERY_TABLE_NAME = "sys_resources res";
    private static String QUERY_TABLE_COLUMN = " res.id,res.name,res.parentid,res.reskey,res.type,res.resurl,res.level,res.ishide,res.description,res.colorid ";
    
    @Override
    public ResourcesDTO save(final ResourcesDTO bean){
        String id = null;
        BaseMap<String, Object> createmap = BeanToMapUtil.copyBeanToMap(bean, new ResourcesDO(), "id");
        try {
            id = baseCreate(createmap, "sys_resources", "id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        bean.setId(id);
        return bean;
    }
    
    @Override
    public void update(BaseMap<String, Object> updatemap,BaseMap<String, Object> whereMap){
        this.baseupdate(updatemap, whereMap, "sys_resources");
    }
    
    @Override
    public Page list(Page page,ResourcesQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        // 原来是有顺序的 前后分离后没有必要了 private static String QUERY_TABLE_COLUMN = "
        // res.id,res.name,res.parentid,res.reskey,res.type,res.resurl,res.level,res.icon,res.ishide,res.description,res.colorid
        // ";
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(",sys_color color");
        sql.append(getcondition(qvo));
        page.setTotals(queryForInt(sql));
        if (page.getTotals() > 0) {
            page.setResult(getJdbcTemplate().query(page.getPagesql(sql), new BeanPropertyRowMapper<ResourcesDTO>(ResourcesDTO.class)));
        }
        else {
            page.setResult(new ArrayList<ResourcesDTO>());
        }
        return page;
    }
    
    private String getcondition(ResourcesQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(WHERE).append("1=1");
        if (QvoConditionUtil.checkInteger(qvo.getIsdelete())) {
            sql.append(AND).append("res.isdelete=").append(qvo.getIsdelete());
        }
        else {
            // 默认
            sql.append(AND).append("res.isdelete=0");
        }
        sql.append(AND).append("res.colorid=color.id");
        sqlappen(sql, "res.colorid", qvo.getColorid());
        sqlappen(sql, "res.id", qvo.getId());
        sqlappen(sql, "res.level", qvo.getLevel());
        sqlappen(sql, "res.description", qvo.getDescription(), qvo);
        sqlappen(sql, "res.icon", qvo.getIcon(), qvo);
        sqlappen(sql, "res.ishide", qvo.getIshide(), qvo);
        sqlappen(sql, "res.name", qvo.getName(), qvo);
        sqlappen(sql, "res.reskey", qvo.getReskey(), qvo);
        sqlappen(sql, "res.resurl", qvo.getResurl(), qvo);
        sqlappen(sql, "res.type", qvo.getType());
        sqlappen(sql, "res.parentid", qvo.getParentid());
        return sql.toString();
    }
    
    @Override
    public List<ResourcesDTO> list(ResourcesQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(",sys_color color");
        sql.append(getcondition(qvo));
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<ResourcesDTO>(ResourcesDTO.class));
    }
    
    @Override
    public List<ResourcesDTO> getRolesByRoleId(String roleid) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append("sys_resources res,sys_res_role rr").append(WHERE).append("res.id=rr.resId");
        sqlappen(sql, "rr.roleid", roleid);
        sql.append(AND).append("rr.state=0");
        sql.append(AND).append("res.isdelete=0");
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<ResourcesDTO>(ResourcesDTO.class));
    }
    
    @Override
    public List<ResourcesDTO> getRolesByRoleIdHaveNull() throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(", IFNULL(sr.rolekey,'ROLR_NULL') rolekey ").append(FROM).append("sys_resources res LEFT JOIN sys_res_role rr").append(ON).append("res.id=rr.resId AND res.isdelete=0");
        sql.append(" LEFT JOIN sys_role sr  ON rr.roleid=sr.id AND rr.state=0  ");
        sql.append(WHERE).append("1=1");
        // sqlappen(sql, "rr.roleid", roleid);
        // sql.append(AND).append("rr.state=0");
        // sql.append(AND).append("res.isdelete=0");
        System.out.println(sql.toString());
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<ResourcesDTO>(ResourcesDTO.class));
    }
    
    @Override
    public List<ResourcesDTO> getOperatorButton(String roleid,String parentid) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(", sys_res_role rr  ").append(",sys_color color");
        sql.append(WHERE).append("res.id=rr.resId").append(AND).append("res.colorid=color.id").append(AND).append("rr.roleid=").append(roleid);
        sqlappen(sql, "rr.roleid", roleid);
        sqlappen(sql, "parentid", parentid);
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<ResourcesDTO>(ResourcesDTO.class));
    }
}
