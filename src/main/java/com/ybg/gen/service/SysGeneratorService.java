package com.ybg.gen.service;

import java.util.List;
import java.util.Map;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.gen.qvo.GeneratorQuery;

/**
 * 代码生成器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午3:33:38
 */
public interface SysGeneratorService {

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param qvo
	 * @param databaseid
	 *            gen_db表的ID
	 * @return
	 * @throws Exception
	 */
	Page list(Page page, GeneratorQuery qvo, String databaseid) throws Exception;

	/**
	 * 查询表
	 * 
	 * @param tableName
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	Map<String, String> queryTable(String tableName, String databaseId) throws Exception;

	/**
	 * 查询列
	 * 
	 * @param tableName
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, String>> queryColumns(String tableName, String databaseId) throws Exception;

	/**
	 * 生成代码
	 * 
	 * @param tableNames
	 * @return
	 * @throws Exception
	 */
	byte[] generatorCode(String[] tableNames, String databaseId, String styleid) throws Exception;

	/** 获取生成配置 **/
	Map<String, String> queryGenSetting();

	/**
	 * 更新生成设置配置
	 * 
	 * @param updatemap
	 * @param wheremap
	 */
	void updateGenSetting(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap);

	/**
	 * 
	 * <p>
	 * Title: 更新生成的文件上下文配置
	 * </p>
	 * 
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param email 注释的email
	 * @param author 注释的作者
	 * @param javapackage 指定的java包
	 * @param tablePrefix 过滤的表名前缀
	 * @param pathName 基本路径
	 * @param styleid 框架ID
	 * @return
	 * 
	 */
	Json updateSetting(String email, String author, String javapackage, String tablePrefix, String pathName,
			String styleid);
}
