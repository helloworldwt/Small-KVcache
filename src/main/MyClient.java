package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 * @author WangTian
 *
 * 2015-1-26
 */

public class MyClient {
    private static final String IP="127.0.0.1";
    private static final int PORT=8888;
    public static void main(String[] args) {
        try {
            Socket s = new Socket(IP,PORT);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入发送消息内容：");
                bw.write(scanner.nextLine()+"\n");   //有换行字符，所以command长度要加1
                bw.newLine();
                bw.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                //读取服务器返回的消息数据
                System.out.println(br.readLine());
                //	System.out.println(s.getInetAddress().getLocalHost()+":"+s.getPort()+">>"+br.readLine()+"~");
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
