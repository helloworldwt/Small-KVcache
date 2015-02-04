package main.thread;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import main.util.BloomFilter.BloomFilter;

/**
 * Created by Wangtian on 2015/2/3.
 * 重写aof文件
 */
public class RewriteAofTask extends TimerTask{
    private String filepath;
    private String newfilepath;
    private int mapsize;

    private long index;             //重写文件大小，写完之后文件指针所在位置
    public RewriteAofTask(String filepath,int mapsize){
        this.filepath=filepath;
        this.mapsize=mapsize;
    }
    public String getNewfilepath(){
        return newfilepath;
    }
    public boolean isOvertime(String command[]){
        boolean isOvertime=false;
        try {
            Date nowtime=new Date(System.currentTimeMillis());     //返回一个一个毫秒数,不用date
            DateFormat format= new SimpleDateFormat("yyyyMMddHHmmss");
            Date date=format.parse(command[4]);
            Calendar Cal=Calendar.getInstance();
            Cal.setTime(date);
            Cal.add(Calendar.SECOND,Integer.parseInt(command[3]));
            isOvertime=nowtime.after(date);         //存的时候存过期时间
        }catch (ParseException e){
            e.printStackTrace();
        }
        return isOvertime;
    }
    @Override
    public void run(){

        try {
            //创建一个重写的aof文件
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");//时间格式可以去掉冒号
            String date = sDateFormat.format(new java.util.Date());
            newfilepath="D:\\rdb\\" + date + ".txt";
            File filename = new File(newfilepath);
            filename.createNewFile();
            //读入旧的aof文件，删掉过期数据，重复数据，淘汰数据
            RandomAccessFile read=new RandomAccessFile(filepath,"rw");
            System.out.println(read.length());
            RandomAccessFile write=new RandomAccessFile(newfilepath,"rw");
            long pos=read.length();        //文件末尾指针
            int i=0;                       //已经保存set命令的数目
            BloomFilter bf=new BloomFilter(); //重复的key只保留一个
            //开始读入，写入
            while(pos>0&&i<mapsize){
                read.seek(pos);
                --pos;
                if(read.readByte()=='\n'){
                    String command[]=read.readLine().split(" ");
                    if(command.length==3){
                        if(!bf.contains(command[1])){
                            bf.add(command[1]);
                            i++;
                            write.seek(write.length());
                            write.write(read.readLine().getBytes());
                        }
                    }
                    else {
                        if (!isOvertime(command)){
                            if(!bf.contains(command[1])){
                                bf.add(command[1]);
                                i++;
                                write.seek(write.length());
                                write.write(read.readLine().getBytes());
                            }
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
//同一个set，来一个set ，lru,aof time 写的时间，隔多少秒写，bloomfilter 误判,bloomfilter hash 算法