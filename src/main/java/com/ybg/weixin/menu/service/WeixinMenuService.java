package com.ybg.weixin.menu.service;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.weixin.api.domain.WeixinJson;
import com.ybg.weixin.menu.domain.WeixinButtonDTO;

/*** @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1 */
public interface WeixinMenuService {
	
	/** c创建
	 * 
	 * @param bean
	 * @throws Exception
	 */
	void create(WeixinButtonDTO bean) throws Exception;
	
	/** 更新
	 * 
	 * @param updatemap
	 * @param wheremap
	 */
	void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap);
	
	/** 删除
	 * 
	 * @param conditionmap
	 */
	void remove(BaseMap<String, Object> conditionmap);
	
	/** 列表
	 * 
	 * @return */
	List<WeixinButtonDTO> list();
	
	/** 获取单个
	 * 
	 * @param id
	 * @return */
	WeixinButtonDTO get(String id);
	
	/** 列表
	 * 
	 * @return */
	List<WeixinButtonDTO> menulist();
	
	/** 根据父级目录查询
	 * 
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	List<WeixinButtonDTO> buttonlist(String parentid) throws Exception;
	
	/** 生成菜单到微信服务器
	 * 
	 * @param list
	 * @return */
	WeixinJson save(List<WeixinButtonDTO> list);
	
	/** 清除微信服务器的菜单
	 * 
	 * @return */
	WeixinJson cleanmenu();
}
