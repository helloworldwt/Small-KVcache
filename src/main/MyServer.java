package main;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.net.ServerSocket;
import java.net.Socket;
import main.thread.PrintAof;
import main.thread.ProcessCommand;

/**
 * 接收命令的socket,和持久化数据
 * @param args
 * @author wangtian
 *
 */
public class MyServer {
    private static final int PORT=8888;
    private static ConcurrentLRUHashMap map=new ConcurrentLRUHashMap(1000,(float)0.75,20);
    private static Queue<String> queue = new LinkedList<String>();

    public static void main(String[] args){
        try{
            ServerSocket ssocket=new ServerSocket(PORT);
            Thread p=new PrintAof(queue);
            p.start();                          //nio ,不适用nio,细节好好想,下周三
            System.out.println("服务器已启动");
            while(true){
               Socket s=ssocket.accept();
               Thread thread = new ProcessCommand(queue,s,map);
               thread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
