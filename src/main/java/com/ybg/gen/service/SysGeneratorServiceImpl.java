package com.ybg.gen.service;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.gen.dao.SysGeneratorDao;
import com.ybg.gen.qvo.GeneratorQuery;
import com.ybg.gen.utils.GenUtils;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/***
 * @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1
 */
@Repository
public class SysGeneratorServiceImpl implements SysGeneratorService {
    
    @Autowired
    private SysGeneratorDao sysGeneratorDao;
    
    @Override
    public Page list(Page page,GeneratorQuery qvo,String databaseid) throws Exception{
        return sysGeneratorDao.list(page, qvo,databaseid);
    }
    
    @Override
    public Map<String, String> queryTable(String tableName,String databaseId) throws Exception{
        return sysGeneratorDao.queryTable(tableName,databaseId);
    }
    
    @Override
    public List<Map<String, String>> queryColumns(String tableName,String databaseId) throws Exception{
        return sysGeneratorDao.queryColumns(tableName,databaseId);
    }
    
    @Override
    public byte[] generatorCode(String[] tableNames,String databaseId,String styleid) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName,databaseId);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName,databaseId);
            // 生成代码
            try {
                GenUtils.generatorCode(table, columns, zip,styleid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    
    @Override
    public Map<String, String> queryGenSetting(){
        return sysGeneratorDao.queryGenSetting();
    }
    
    @Override
    public void updateGenSetting(BaseMap<String, Object> updatemap,BaseMap<String, Object> wheremap){
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
    }

	@Override
	public Json updateSetting(String email, String author, String javapackage, String tablePrefix, String pathName,
			String styleid) {
		Json j = new Json();
        j.setMsg("操作成功");
        BaseMap<String, Object> updatemap = new BaseMap<>();
        BaseMap<String, Object> wheremap = new BaseMap<>();
        updatemap.put("`value`", email);
        wheremap.put("`key`", "email");
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
        updatemap = new BaseMap<>();
        wheremap = new BaseMap<>();
        updatemap.put("`value`", author);
        wheremap.put("`key`", "author");
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
        updatemap = new BaseMap<>();
        wheremap = new BaseMap<>();
        updatemap.put("`value`", tablePrefix);
        wheremap.put("`key`", "tablePrefix");
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
        updatemap = new BaseMap<>();
        wheremap = new BaseMap<>();
        updatemap.put("`value`", javapackage);
        wheremap.put("`key`", "package");
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
        updatemap = new BaseMap<>();
        wheremap = new BaseMap<>();
        updatemap.put("`value`", pathName);
        wheremap.put("`key`", "pathName");
        sysGeneratorDao.updateGenSetting(updatemap, wheremap);
        j.setSuccess(true);
        return j;
	}
}
