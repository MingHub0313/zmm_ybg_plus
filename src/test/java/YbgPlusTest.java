// import cn.hutool.core.bean.BeanUtil;
// import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
// import com.ybg.base.jdbc.BaseMap;
// import com.ybg.base.jdbc.DataBaseConstant;
// import com.ybg.base.util.BeanToMapUtil;
// import com.ybg.rbac.role.domain.BatchResRoleVO;
// import com.ybg.rbac.user.domain.UserVO;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.SpringBootConfiguration;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Primary;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.datasource.DataSourceTransactionManager;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.Transactional;

// import javax.sql.DataSource;
// import java.util.Map;

// @RunWith(SpringRunner.class)
// // @SpringApplicationConfiguration(classes = YbgPlusTest.class)
// //@WebAppConfiguration
// //@Transactional
// //@SpringBootConfiguration()
// @SpringBootTest(classes = App.class)

// public class YbgPlusTest {




// 	@Autowired
// 	@Qualifier("sysJdbcTemplate")
// 	JdbcTemplate jdbcTemplate;

// 	@Test
// 	public void test1() {
// 		UserVO user= new UserVO();
// 		user.setUsername("aaa");
// 		user.setId("id");
// 		BatchResRoleVO bean= new BatchResRoleVO();
// 		BaseMap<String,Object> map= BeanToMapUtil.beanToMap(user,"id");
// //        BaseMap<String,Object> newmap=new BaseMap<>();
// //        Map<String,Object> map= BeanUtil.beanToMap(bean,newmap,true,true);
// //        map= BeanUtil.beanToMap(bean,true,true);
// 	//	System.out.println(newmap.toString());
//         System.out.println(map.toString());
// 	}
// }
