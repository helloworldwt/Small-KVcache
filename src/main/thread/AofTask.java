package main.thread;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Queue;
import java.util.TimerTask;

/**
 * Created by Wangtian on 2015/2/3.
 * 写aof文件定时线程
 */
public class AofTask extends TimerTask{
    private String pathname;
    private volatile Queue<String> queue;
    public AofTask(Queue<String> queue,String pathname){
        this.queue=queue;
        this.pathname=pathname;
    }

    @Override
    public void run(){
        try {
            //对aof文件进行写入
            String str="";
            while (queue.size()>0){
                str+=queue.poll()+"\n";
            }
            RandomAccessFile write=new RandomAccessFile(pathname,"rw");
            write.seek(write.length());
            write.write(str.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
