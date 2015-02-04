package test;
import main.ConcurrentLRUHashMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by lenovo on 2015/2/2.
 *
 *测试concurrenthashmap的设置超时功能
 */
public class TestExpiration {
    public static void main(String[] args){
        ConcurrentLRUHashMap<String, String> map=new ConcurrentLRUHashMap<String,String>(1000,(float)0.75,1);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");//时间格式可以去掉冒号
        String date = sDateFormat.format(new java.util.Date());
        String time[]=date.split(":");
        int j=0;
        for(int i=0;i<time.length;i++){

        }
        //处理日期
        java.util.Calendar Cal=java.util.Calendar.getInstance();
        Date t=new Date(System.currentTimeMillis());
        Date ttt;
        Date tttt;
        Cal.setTime(t);
        System.out.println(sDateFormat.format(Cal.getTime()));
        ttt=Cal.getTime();
        Cal.add(Calendar.SECOND,10);
        tttt=Cal.getTime();
        System.out.println("date:"+sDateFormat.format(Cal.getTime()));
        System.out.println(ttt.before(tttt));

        Date tt=new Date();
        map.put("key","h",10);
        map.put("test1","h",1);
        map.remove("key");
        for (Map.Entry<String,String> entry : map.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
        System.out.print(tt.after(new Date(System.currentTimeMillis())));
//        System.out.print(format.format(t));
    }

}
