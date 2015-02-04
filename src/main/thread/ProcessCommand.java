package main.thread;

import main.ConcurrentLRUHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Queue;

/**
 * @author WangTian
 * @ 处理请求命令，线程
 * 2015-1-29
 */
public class ProcessCommand extends Thread{
    private Queue<String> queue;
    private Socket client;
    private ConcurrentLRUHashMap map;
    public ProcessCommand(Queue<String> queue,Socket client,ConcurrentLRUHashMap map){
        this.queue=queue;
        this.client=client;
        this.map=map;
    }
    public void run(){
        try{
            while (true){
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String mess = br.readLine();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                //输出返回值，写打印队列
                String[] command=mess.split(" ");
                String str="";

                if(command[0].equals("set")&&!command[1].isEmpty()&&!command[2].isEmpty()&&command.length==3){
                    map.put(command[1], command[2]);
                    str += "true\n";
                    queue.offer(mess);
                    //  System.out.println(command.length);
                }
                else if(command[0].equals("set")&&!command[1].isEmpty()&&!command[2].isEmpty()&&!command[3].isEmpty()){
                    map.put(command[1], command[2],Integer.parseInt(command[3])); // command安全验证,bloomhash,hash算法,lru,key ,没了.
                    str += "true\n";
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");//时间格式可以去掉冒号
                    String date = sDateFormat.format(new java.util.Date());
                    queue.offer(mess+" "+date);
                }
                else if(command[0].equals("get")&&!command[1].isEmpty()){
                    str+=map.get(command[1])+"";
                    if(str.equals("null"))
                        str+="\n";
                    else
                        str += map.get(command[1])+"\n";
                }

              //  queue.offer(mess);   //如果设置了超时，则记下此刻时间

                bw.write(str);
                bw.flush();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
