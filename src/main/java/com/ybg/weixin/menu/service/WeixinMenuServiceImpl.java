package com.ybg.weixin.menu.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.weixin.api.domain.WeixinJson;
import com.ybg.weixin.api.network.WeixinNetWork;
import com.ybg.weixin.api.service.WeixinApiService;
import com.ybg.weixin.menu.WeixinButtonConstant;
import com.ybg.weixin.menu.dao.WeixinButtonDao;
import com.ybg.weixin.menu.domain.WeixinButtonDTO;
import net.sf.json.JSONObject;

/*** @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1 */
@Repository
public class WeixinMenuServiceImpl implements WeixinMenuService {
	
	@Autowired
	WeixinNetWork		weixinNW;
	@Autowired
	WeixinApiService	weixinApiService;
	@Autowired
	WeixinButtonDao		weixinButtonDao;
	
	private String getAccessToken() {
		return weixinApiService.getAccessToken();
	}
	
	@Override
	public void create(WeixinButtonDTO bean) throws Exception {
		weixinButtonDao.create(bean);
	}
	
	@Override
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap) {
		weixinButtonDao.update(updatemap, wheremap);
	}
	
	@Override
	public void remove(BaseMap<String, Object> conditionmap) {
		weixinButtonDao.remove(conditionmap);
	}
	
	@Override
	public List<WeixinButtonDTO> list() {
		return weixinButtonDao.list();
	}
	
	@Override
	public WeixinButtonDTO get(String id) {
		return weixinButtonDao.get(id);
	}
	
	@Override
	public List<WeixinButtonDTO> menulist() {
		return weixinButtonDao.menulist();
	}
	
	@Override
	public List<WeixinButtonDTO> buttonlist(String parentid) throws Exception {
		return weixinButtonDao.buttonlist(parentid);
	}
	
	@Override
	public WeixinJson save(List<WeixinButtonDTO> list) {
		JSONObject menu = new JSONObject();
		Collections.sort(list);
		// 已经排序好了
		// 一 组合成适合的树状结构。
		List<WeixinButtonDTO> menuList = new ArrayList<WeixinButtonDTO>();
		for (WeixinButtonDTO bean : list) {
			if (bean.getParentid() == null) {
				menuList.add(bean);
			}
		}
		for (WeixinButtonDTO bean : menuList) {
			for (WeixinButtonDTO beanr : list) {
				if (beanr.getParentid() != null) {
					if (beanr.getIfsub() == WeixinButtonConstant.IFSUBYES && beanr.getParentid().equals(bean.getId())) {
						bean.getSub_button().add(beanr);
					}
				}
			}
		}
		menu.put("button", menuList);
		return weixinNW.menu_create(menu, getAccessToken());
	}
	
	@Override
	public WeixinJson cleanmenu() {
		return weixinNW.menu_delete(getAccessToken());
	}
}
