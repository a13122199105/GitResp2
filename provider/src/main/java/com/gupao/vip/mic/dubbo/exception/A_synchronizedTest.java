package com.gupao.vip.mic.dubbo.exception;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.sun.codemodel.internal.JForEach;

import java.util.concurrent.CountDownLatch;

//多线程面试题  用两个线程输出 1a2b3c...等等
public class A_synchronizedTest {
    private static Thread T1;
    private static Thread T2;
    //synchronized wait  countDownLatch()
    //lockSupport()
    //condition  (这个可以指定锁)
    public static char[] arr = {'1','2','3'};
    public static char[] brr = {'a','b','c'};
    private volatile static boolean T2statess = false;
    public static CountDownLatch countDownLatch = new CountDownLatch(1);

        public static void main(String[] args) {


        final Object obj = new Object();
        T1 = new Thread(new Runnable() {
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                synchronized (obj){
                   /* if (!T2statess){
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                        for(char a : arr){
                            System.out.print(a);
                        try {

                            obj.notify();
                            obj.wait(); //让出锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    obj.notify();  //必须否则无法停止程序
                }
            }
        });
        T1.start();

        T2 = new Thread(new Runnable() {
            public void run() {

                synchronized (obj){
                    for(char b : brr){
                        System.out.print(b);
                        countDownLatch.countDown();
                        try {
                            //T2statess = true;
                            obj.notify();
                            obj.wait(); //让出锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    obj.notify(); //必须否则无法停止程序
                }
            }
        });
        T2.start();


    }
}
