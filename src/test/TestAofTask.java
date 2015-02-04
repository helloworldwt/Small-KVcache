package test;

import main.thread.AofTask;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by Wangtian on 2015/2/3.
 * 测试aoftask
 */
public class TestAofTask {
    private static String pathname="D:\\rdb\\20150202042223.txt";
    private static Queue<String> queue=new LinkedList<String>();
    public static void main(String[] args){
        queue.offer("set test h");
        queue.offer("set tes h");
        queue.offer("set te h");
        queue.offer("set t h");
        Timer t=new Timer();
        t.schedule(new AofTask(queue,pathname),1000,1000);
    }

}
