package main.util;

import java.io.IOError;

/**
 * Created by Wangtian on 2015/2/5.
 * 改造hashmap，只存储key，不存储vlaue
 */
public class KeyMap {
    /**
     * 存储key的keyentry形式的默认数组长度
     */
    private static final int length=10000;
    /**
     * 存储key的数组
     */
    private static keyentry table[];
    /**
     * 静态的内部类key实体
     */
    static class keyentry{
        String key;
        int hash;
        keyentry next;
        keyentry(String key,int hash,keyentry next){
            this.key=key;
            this.hash=hash;
            this.next=next;
        }
    }

    /**
     * 对hashcode再hash，让hashcode的高位也对hash值有影响
     * @param h
     * @return
     */
    public int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * 找key在数组中的位置
     * @param h
     * @param length
     * @return
     */
    public int indexFor(int h, int length) {
        return h & (length-1);
    }

    /**
     * 判断key是否在map中
     * @param key
     * @return
     */
    public boolean isContainKey(String key){
        if(key==null) throw new NullPointerException();
        keyentry e=table[indexFor(key.hashCode(),length)];
        while (e!=null){
            if(e.equals(key))
                return true;
            e=e.next;
        }
        return false;
    }

    /**
     * 对key进行插入，如果存在key则返回false，否则进行插入返回true
     * @param key
     * @return
     */
    public boolean put(String key){
        if(isContainKey(key)) return  false;
        keyentry e;
        int h=hash(key.hashCode());
        int i=indexFor(h,length);
        if(table[i]==null){
            table[i]=new keyentry(key,h,null);
        }
        else {
            e=table[i];
            while (e.next!=null){
                e=e.next;
            }
            e.next=new keyentry(key,h,null);
        }
        return true;

    }

    /**
     * 构造函数，初始化一个table
     */
    public KeyMap(){
        table=new keyentry[length];
    }
}
