package main.thread;

import main.ConcurrentLRUHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String mess = br.readLine();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                //正则表达式进行匹配，错误输入提示重新输入
                String regex="^set\\s[^\\s]{1,}\\s[^\\s]{1,}(\\s[\\d]{1,7})?|^get\\s[^\\s]{1,}|^delete\\s[^\\s]{1,}|^stats";
                Pattern pattern=Pattern.compile(regex);
                Matcher matcher=pattern.matcher(mess);
                //输出字符
                String str = "";
                if(matcher.matches()){
                    String[] command = mess.split(" ");
                    if (command[0].equals("set")&& command.length == 3) {
                        map.put(command[1], command[2]);
                        str += "true\n";
                        queue.offer(mess);}
                    else if (command[0].equals("set") && command.length==4) {
                        map.put(command[1], command[2], Integer.parseInt(command[3]));
                        str += "true\n";
                        long overt=System.currentTimeMillis()+Integer.parseInt(command[3])*1000;
                        queue.offer(command[0] + " " + command[1] + " " +command[2] + " " +overt);}
                    else if (command[0].equals("get")) {
                        str += map.get(command[1]) + "";
                        if (str.equals("null"))
                            str += "\n";
                        else
                            str += "\n";}
                    else if(command[0].equals("delete")){
                        map.remove(command[1]);
                        str+="remove "+command[1]+"!\n";
                    }
                }
                //非法输入，输出error
                else str+="error!\n";
                bw.write(str);
                bw.flush();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
