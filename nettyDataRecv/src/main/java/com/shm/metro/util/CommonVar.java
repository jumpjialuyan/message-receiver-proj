package com.shm.metro.util;/**
 * Created by Jerry on 2023/12/25.
 */

import com.shm.metro.bean.ReloadResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用变量
 *
 * @author Jerry
 * @create 2023-12-25 上午10:29
 **/

public class CommonVar {
    public static final Map<String,ReloadResult> RELOAD_REUSLT = new ConcurrentHashMap<>();

    public final static String JAR_FILE_PATH_CONFIG = Util.getConfig().getProperty("jar_path_config");

    public final static String DEFAILT_JAR_FILE_PATH = Util.getConfig().getProperty("init_jar_path");

}
