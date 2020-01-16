package com.ybg.gen.dao;
import java.util.ArrayList;
import java.util.List;
import com.ybg.base.util.BeanToMapUtil;
import com.ybg.gen.domain.GenTempDO;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Page;
import com.ybg.gen.domain.GenTempDTO;
import com.ybg.gen.qvo.GenTempQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ybg.base.jdbc.DataBaseConstant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Repository
public class GenTempDaoImpl extends BaseDao implements GenTempDao {
    
    @Autowired
    @Qualifier(DataBaseConstant.JD_SYS)
    JdbcTemplate jdbcTemplate;
    
    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
    
    private static String QUERY_TABLE_COLUMN = " genTemp.styleid,genTemp.description,genTemp.genfilename,genTemp.gencontext,genTemp.state, genTemp.id";
    private static String QUERY_TABLE_NAME = "sys_gen_temp  genTemp";
    
    @Override
    public GenTempDTO save(GenTempDTO genTemp) throws Exception{
        BaseMap<String, Object> createmap = BeanToMapUtil.copyBeanToMap(genTemp, new GenTempDO(), "id");
        String id = null;
        id = baseCreate(createmap, "sys_gen_temp", "id");
        genTemp.setId((String) id);
        return genTemp;
    }
    
    @Override
    public void update(BaseMap<String, Object> updatemap,BaseMap<String, Object> WHEREmap){
        this.baseupdate(updatemap, WHEREmap, "sys_gen_temp");
    }
    
    @Override
    public Page list(Page page,GenTempQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
        sql.append(getcondition(qvo));
        page.setTotals(queryForInt(sql));
        if (page.getTotals() > 0) {
            page.setResult(getJdbcTemplate().query(page.getPagesql(sql), new BeanPropertyRowMapper<GenTempDTO>(GenTempDTO.class)));
        }
        else {
            page.setResult(new ArrayList<GenTempDTO>());
        }
        return page;
    }
    
    private String getcondition(GenTempQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(WHERE).append("1=1");
        // if (QvoConditionUtil.checkInteger(qvo.getIsdelete())) {
        // sql.append(AND).append("genTemp.isdelete=").append(qvo.getIsdelete());
        // } else {
        // sql.append(AND).append("genTemp.isdelete=0");// 默认
        // }
        sqlappen(sql, "genTemp.id", qvo.getId());
        sqlappen(sql, "genTemp.description", qvo.getDescription());
        sqlappen(sql, "genTemp.genfilename", qvo.getGenfilename());
        sqlappen(sql, "genTemp.gencontext", qvo.getGencontext());
        sqlappen(sql, "genTemp.state", qvo.getState());
        sqlappen(sql, "genTemp.styleid", qvo.getStyleid());
        return sql.toString();
    }
    
    @Override
    public List<GenTempDTO> list(GenTempQuery qvo) throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
        sql.append(getcondition(qvo));
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<GenTempDTO>(GenTempDTO.class));
    }
    
    @Override
    public void remove(BaseMap<String, Object> wheremap){
        baseremove(wheremap, "sys_gen_temp");
    }
    
    @Override
    public GenTempDTO get(String id){
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(FROM).append(QUERY_TABLE_NAME);
        sql.append(WHERE).append("1=1");
        sql.append(AND).append("id='" + id + "'");
        List<GenTempDTO> list = getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<GenTempDTO>(GenTempDTO.class));
        return QvoConditionUtil.checkList(list) ? list.get(0) : null;
    }
}
