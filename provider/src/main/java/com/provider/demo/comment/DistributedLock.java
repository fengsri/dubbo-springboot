package com.provider.demo.comment;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author fengwen
 * @Date 2020/1/22 18:12
 * @Version V1.0
 */
public class DistributedLock {
    public static Logger log = LoggerFactory.getLogger(DistributedLock.class);

    //可重入排它锁
    private InterProcessMutex interProcessMutex;
    //竞争资源标志
    private String lockName;
    //根节点
    private String root = "/distributed/lock/";

    private static CuratorFramework curatorFramework;

    private static String ZK_URL = "127.0.0.1:2181";

    static{
        curatorFramework= CuratorFrameworkFactory.newClient(ZK_URL,new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
    }

    /**
     * 实例化
     * @param lockName
     */
    public DistributedLock(String lockName){
        try {
            this.lockName = lockName;
            interProcessMutex = new InterProcessMutex(curatorFramework, root + lockName);
        }catch (Exception e){
            log.error("initial InterProcessMutex exception="+e);
        }
    }

    /**
     * 获取锁
     */
    public void acquireLock(){
        try {
            //重试2次，每次最大等待2s，也就是最大等待4s
            while (true){
                boolean acquire = interProcessMutex.acquire(2, TimeUnit.SECONDS);
                if (acquire){
                    log.info("Thread:"+Thread.currentThread().getId()+" acquire distributed lock  success");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("distributed lock acquire exception="+e);
        }
    }

    /**
     * 释放锁
     */
    public void releaseLock(){
        try {
            if(interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()){
                interProcessMutex.release();
                curatorFramework.delete().inBackground().forPath(root+lockName);
                log.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  success");
            }
        }catch (Exception e){
            log.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  exception="+e);
        }
    }
}
