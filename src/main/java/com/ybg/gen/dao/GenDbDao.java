package com.ybg.gen.dao;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Page;
import  com.ybg.gen.domain.GenDbDTO;
import  com.ybg.gen.qvo.GenDbQuery;
/**
 * 代码生成数据库列表
 * 
 * @author Deament
 * @email 591518884@qq.com
 * @date 2018-05-30
 */
public interface GenDbDao {
	/**
	 * 返回主键的创建
	 * 
	 * @param bean
	 *            主要看DO字段
	 * @return 实体（可以得到主键）
	 * @throws Exception
	 */
	GenDbDTO save(GenDbDTO bean) throws Exception;

	/**
	 * 更新数据，条件 和 需要更新的字段都不能为空 不限个数个条件
	 * 
	 * @author Deament
	 * @param updatemap
	 *            需要更新的字段和值
	 * @param wheremap
	 *            更新中的条件字段和值
	 **/
	void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap);

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            分页类
	 * @param qvo
	 *            查询实体
	 * @return 分页类
	 * @throws Exception
	 */
	Page list(Page page, GenDbQuery qvo) throws Exception;

	/**
	 * 不分页查询
	 * 
	 * @param qvo
	 *            查询实体
	 * @return List列表
	 * @throws Exception
	 */
	List<GenDbDTO> list(GenDbQuery qvo) throws Exception;

	/**
	 * 根据条件删除
	 * 
	 * @param wheremap
	 *            条件MAP
	 */
	void remove(BaseMap<String, Object> wheremap);

	/**
	 * 查询单个
	 * 
	 * @param id
	 *            表的逐渐
	 * @return 实体
	 */
	GenDbDTO get(String id);
}
