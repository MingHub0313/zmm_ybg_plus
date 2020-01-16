package com.ybg.base.util;
import cn.hutool.core.bean.BeanUtil;
import com.ybg.base.jdbc.BaseMap;
import org.springframework.beans.BeanUtils;
import java.util.Map;

/***
 * @author https://gitee.com/YYDeament/88ybg
 *         用于操作数据库类
 * @date 2016/10/1
 */
public class BeanToMapUtil {
    
    private BeanToMapUtil(){
    }
    
    /**
     * @param bean
     *            类
     * @param ignorekey
     *            忽略的key
     * @return
     **/
    public static BaseMap<String, Object> beanToMap(Object bean,String... ignorekey){
        BaseMap<String, Object> newmap = new BaseMap<>();
        Map<String, Object> map = BeanUtil.beanToMap(bean, false, true);
        for (String key : map.keySet()) {
            newmap.put(key, map.get(key));
        }
        for (String key : ignorekey) {
            newmap.remove(key);
        }
        return newmap;
    }
    
    /**
     * 主要用于dao层save方法 把视图对象转换成数据库操作对象再转换成非空map 返回去
     * 
     * @param source
     *            元类
     * @param targetbean
     *            目标类
     * @param ignorekey
     *            忽略的key
     * @return
     **/
    public static BaseMap<String, Object> copyBeanToMap(Object source,Object targetbean,String... ignorekey){
        BaseMap<String, Object> newmap = new BaseMap<String, Object>();
        BeanUtils.copyProperties(source, targetbean);
        Map<String, Object> map = BeanUtil.beanToMap(targetbean, false, true);
        for (String key : map.keySet()) {
            newmap.put(key, map.get(key));
        }
        for (String key : ignorekey) {
            newmap.remove(key);
        }
        return newmap;
    }
}
