package com.ybg.gen.service;
import com.ybg.gen.domain.GenTempDTO;
import com.ybg.gen.qvo.GenTempQuery;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;

/**
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-02-05
 */
public interface GenTempService {
    
    /**
     * 返回主键的创建
     * 
     * @throws Exception
     **/
    GenTempDTO save(GenTempDTO bean) throws Exception;
    
   /**更新**/
    Json update(GenTempDTO bean);
    
    /** 分页查询 **/
    // sys_role
    Page list(Page page,GenTempQuery qvo) throws Exception;
    
    /** 不分页查询 **/
    // sys_role
    List<GenTempDTO> list(GenTempQuery qvo) throws Exception;
    
    /** 根据条件删除 **/
    void remove(BaseMap<String, Object> wheremap);
    
    GenTempDTO get(String id);
}
