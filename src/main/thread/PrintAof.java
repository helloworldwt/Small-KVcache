package main.thread;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Queue;

/**
 * @author WangTian
 * @持久化到磁盘的异步线程
 * 2015-1-29
 */
public class PrintAof extends Thread{
    private Queue<String> queue;
    public PrintAof(Queue<String> queue){
        this.queue=queue;
    }
    public void run(){
        try{
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");//时间格式可以去掉冒号
            String date = sDateFormat.format(new java.util.Date());
            File filename = new File("D:\\rdb\\" + date + ".txt");
            filename.createNewFile();
            while(true){    //循环什么时候退出,cpu核占用,阻塞,事件,退出,开线程试一下，set，put,时间。使用
                if(queue.size()!=0){   //hashmap的时间,chunk大小,memcached和redis对比。load数据
                    BufferedWriter out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,true)));
                    out.write(queue.poll()+" "); //s特殊字符，转义字符,可以每次都写完,写socket规范模式
                    out.flush();
                    out.close();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
