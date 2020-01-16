package com.ybg.base.jdbc;
/**
 * * @author https://gitee.com/YYDeament/88ybg
 * 
 * 
 * @date 2016/10/1<br>
 *       数据库常量
 **/
public class DataBaseConstant {
    
    /** 数据源-系统管理 (默认) **/
    public static final String DB_SYS = "dataSourceSys";
    /** 数据源-定时任务框架 **/
    public static final String DB_QUARTZ = "dataSourceQuartz";
    /** 数据源-报表填报 **/
    public static final String DB_REPORT = "dataSourceRreport";
    // 分隔线-----------------------------------------------
    /** JDBC模板方法-使用系统数据库 (默认) **/
    public static final String JD_SYS = "sysJdbcTemplate";
    /** JDBC模板方法-使用定时任务数据库 **/
    public static final String JD_QUARTZ = "quartzJdbcTemplate";
    /** JDBC模板方法-使用报表填报数据库 **/
    public static final String JD_REPORT = "reportJdbcTemplate";
    // 分割线
    /**** 数据库事务-使用系统数据库 *********/
    public static final String TM_SYS = "sysTransactionManager";
    /** 数据库事务-使用定时任务数据库 **/
    public static final String TM_QUARTZ = "quartzTransactionManager";
    /** 数据库事务-使用报表填报数据库 **/
    public static final String TM_REPORT = "reportTransactionManager";
    // 分割线
    /** 线程切换数据源 **/
    public static ThreadLocal<String> JD_THREAD = new ThreadLocal<>();
    
    /**
     * 设置 当前 线程切换的数据库
     * 
     * @param jdbctimplate
     *            数据库Bean 别名 如 DataBaseConstant.JD_SYS
     */
    public static void setJdbcTemplate(String jdbctimplate){
        JD_THREAD.set(jdbctimplate);
    }
    
    public static void removeJdbcTemplate(){
    	JD_THREAD.remove();
    }
    
    /**
     * 获取当前线程的数据库
     * 
     * @return 数据库Bean 别名
     */
    public static String getJdbcTemplate(){
        return JD_THREAD.get();
    }
}
