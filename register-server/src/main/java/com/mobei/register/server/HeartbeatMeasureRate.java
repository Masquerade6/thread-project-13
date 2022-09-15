package com.mobei.register.server;

/**
 * 心跳测量计数器
 *
 * @author liuyaowu
 * @date 2022/9/16 7:29
 * @remark
 */
public class HeartbeatMeasureRate {

    /**
     * 单例实例
     */
    private static HeartbeatMeasureRate instance = new HeartbeatMeasureRate();

    /**
     * 最近一分钟的心跳次数
     */
    private long latestMinuteHeartbeatRate = 0L;
    /**
     * 最近一分钟的时间戳
     */
    private long latestMinuteTimestamp = System.currentTimeMillis();

    private HeartbeatMeasureRate() {
        Daemon daemon = new Daemon();
        daemon.setDaemon(true);
        daemon.start();
    }

    /**
     * 获取单例实例
     *
     * @return
     */
    public static HeartbeatMeasureRate getInstance() {
        return instance;
    }

    /**
     * 增加一次最近一分钟的心跳次数
     */
    public void increment() {
        synchronized (HeartbeatMeasureRate.class) {
            latestMinuteHeartbeatRate++;
        }
    }

    /**
     * 获取最近一分钟的心跳次数
     */
    public synchronized long get() {
        return latestMinuteHeartbeatRate;
    }

    private class Daemon extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (HeartbeatMeasureRate.class) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - latestMinuteTimestamp > 60 * 1000) {
                            latestMinuteHeartbeatRate = 0L;
                            latestMinuteTimestamp = System.currentTimeMillis();
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}