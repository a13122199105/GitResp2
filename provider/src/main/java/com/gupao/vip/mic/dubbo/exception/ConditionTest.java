package com.gupao.vip.mic.dubbo.exception;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

//多线程面试题  用两个线程输出 1a2b3c...等等
public class ConditionTest {

    public static char[] arr = {'1','2','3'};
    public static char[] brr = {'a','b','c'};
    private static Thread T1;
    private static Thread T2;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws InterruptedException{

        final Lock lock = new ReentrantLock();
        final Condition condition1 = lock.newCondition();
        final Condition condition2 = lock.newCondition();

        T1 = new Thread(new Runnable() {
            public void run() {
                try {
                    countDownLatch.await();
                    lock.lock();//锁住

                    for(char a : arr){
                        System.out.print(a);
                        condition2.signal();
                        condition1.await();
                    }
                    condition2.signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }

            }
        });
        T1.start();

        T2 = new Thread(new Runnable() {
            public void run() {
                try {
                    lock.lock();//锁住

                    for(char b : brr){
                        System.out.print(b);
                        countDownLatch.countDown();
                        condition1.signal();
                        condition2.await();
                    }
                    condition1.signal();

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        T2.start();


    }
}
