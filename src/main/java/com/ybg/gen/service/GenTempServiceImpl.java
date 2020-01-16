package com.ybg.gen.service;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ybg.gen.dao.GenTempDao;
import com.ybg.gen.domain.GenTempDTO;
import com.ybg.gen.qvo.GenTempQuery;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.base.util.ValidatorUtils;

@Repository
public class GenTempServiceImpl implements GenTempService {
    
    @Autowired
    private GenTempDao genTempDao;
    
    @Override
    /**
     * 返回主键的创建
     * 
     * @throws Exception
     **/
    public GenTempDTO save(GenTempDTO bean) throws Exception{
        return genTempDao.save(bean);
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
    public Json update(GenTempDTO genTemp){
    	Json j = new Json();
		j.setSuccess(true);
		ValidatorUtils.validateEntity(genTemp);
		try {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			updatemap.put("description", genTemp.getDescription());
			updatemap.put("genfilename", genTemp.getGenfilename());
			updatemap.put("gencontext", genTemp.getGencontext());
			updatemap.put("state", genTemp.getState());
			updatemap.put("styleid", genTemp.getStyleid());
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			wheremap.put("id", genTemp.getId());
			genTempDao.update(updatemap, wheremap);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
    }
    
    /** 分页查询 **/
    @Override
    public Page list(Page page,GenTempQuery qvo) throws Exception{
        return genTempDao.list(page, qvo);
    }
    
    /** 不分页查询 **/
    @Override
    public List<GenTempDTO> list(GenTempQuery qvo) throws Exception{
        return genTempDao.list(qvo);
    }
    
    /** 根据条件删除 **/
    public void remove(BaseMap<String, Object> wheremap){
        genTempDao.remove(wheremap);
    }
    
    public GenTempDTO get(String id){
        return genTempDao.get(id);
    }

	
}
