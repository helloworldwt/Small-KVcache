package main;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import main.thread.AofTask;
import main.thread.ProcessCommand;
import main.thread.RewriteAofTask;

/**
 * 接收命令的socket,和持久化数据
 * @author wangtian
 *
 */
public class MyServer {
    private static final int PORT=8888;
    private static String pathname;
    private static ConcurrentLRUHashMap map=new ConcurrentLRUHashMap(1000,(float)0.75,20);
    private static Queue<String> queue = new LinkedList<String>();

    public static void main(String[] args){
        try{
            //创建一个aof文件
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");//时间格式可以去掉冒号
            String date = sDateFormat.format(new java.util.Date());
            pathname="D:\\rdb\\" + date + ".txt";
            File filename = new File("D:\\rdb\\" + date + ".txt");
            filename.createNewFile();
            //创建一个线程池
            Executor service = Executors.newCachedThreadPool();
            //监听8888端口
            ServerSocket ssocket=new ServerSocket(PORT);
            Timer t=new Timer();
            t.schedule(new AofTask(queue,pathname),1000,1000);
            //启动重写任务，每天早上4点重写一次
            Timer rt=new Timer();
         //   t.schedule(new RewriteAofTask(pathname,10),1000,1000);
            System.out.println("服务器已启动");
            while(true){
               Socket s=ssocket.accept();
               Thread thread = new ProcessCommand(queue,s,map);
               service.execute(thread);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
