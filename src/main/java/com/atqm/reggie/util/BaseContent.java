package com.atqm.reggie.util;

public class BaseContent {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 得到当前登录员工id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

    /**
     * 保存当前登录员工id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
}
