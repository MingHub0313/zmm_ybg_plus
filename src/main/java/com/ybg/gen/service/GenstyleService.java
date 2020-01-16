package com.ybg.gen.service;

import com.ybg.gen.domain.GenstyleDTO;
import com.ybg.gen.qvo.GenstyleQuery;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;

/**
 * 生成框架风格列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-31
 */
public interface GenstyleService {
	
	/** 返回主键的创建
	 * 
	 * @param bean
	 *            主要看DO字段
	 * @return 实体（可以得到主键）
	 * @throws Exception
	 */
	GenstyleDTO save(GenstyleDTO bean) throws Exception;
	
	
	Json update(GenstyleDTO bean);
	
	/** 分页查询
	 * 
	 * @param page 分页类
	 * @param qvo 查询实体
	 * @return 分页类
	 * @throws Exception
	 */
	Page list(Page page,GenstyleQuery qvo)throws Exception;
	
	/** 不分页查询
	 * 
	 * @param qvo 查询实体
	 * @return List列表
	 * @throws Exception
	 */
	List<GenstyleDTO> list(GenstyleQuery qvo)throws Exception;
	/** 根据条件删除
	 * 
	 * @param wheremap 条件MAP
	 */
	void remove(BaseMap<String, Object> wheremap);
	/** 查询单个
	 * 
	 * @param id 表的逐渐
	 * @return 实体*/
	GenstyleDTO get(String id);

	/**复制框架以及模版
	 * @throws Exception **/
	Json copyStyle(String id) throws Exception;
}
