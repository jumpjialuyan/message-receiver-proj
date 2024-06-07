import com.alibaba.fastjson.JSONObject;
import com.shm.metro.function.Function;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.util.MySqlUtil;

import javax.crypto.Mac;
import java.io.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/9/10.
 */
public class Test
{
    static String str = "/Users/lixiaohuan/Downloads/faultInfo.txt";
    static int start=374;
    static Map<String,Map<String,String>> faultInfo = new HashMap();
    public static void main(String[] args) {
        String [] trainNos = new String[]{"1701","1702","1703","1704","1705","1706","1707","1708","1708",
            "1709","1710","1711","1712","1713","1714","1715","1716","1717","1718","1719","1720"};

        for(String trainNo:trainNos){
            System.out.println((int)(Math.random()*100)%15);
        }

    }


}
