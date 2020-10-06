package com.gupao.vip.mic.dubbo.exception;

import java.util.concurrent.locks.LockSupport;

//多线程面试题  用两个线程输出 1a2b3c...等等
public class LockSuportTest {

    public static char[] arr = {'1','2','3'};
    public static char[] brr = {'a','b','c'};
    private static Thread T1;
    private static Thread T2;
    public static void main(String[] args) {

        T1 = new Thread(new Runnable() {
            public void run() {

            for(char a : arr){
                System.out.print(a);
                LockSupport.unpark(T2);
                LockSupport.park();
            }
            }
        });
        T1.start();

        T2 = new Thread(new Runnable() {
                public void run() {

            for(char b : brr){
                LockSupport.park();
                System.out.print(b);
                LockSupport.unpark(T1);
            }
        }
        });
        T2.start();
    }
}
