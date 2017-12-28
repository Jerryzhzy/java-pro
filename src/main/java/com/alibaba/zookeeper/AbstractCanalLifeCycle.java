package com.alibaba.zookeeper;

/**
 * Created by ziyu.zhang on 2017/12/28.
 * Description 请用一句话描述
 */
public class AbstractCanalLifeCycle {
    protected volatile boolean running = false; // 是否处于运行中

    public boolean isStart() {
        return running;
    }

    public void start() {
        if (running) {
            throw new CanalException(this.getClass().getName() + " has startup , don't repeat start");
        }

        running = true;
    }

    public void stop() {
        if (!running) {
            throw new CanalException(this.getClass().getName() + " isn't start , please check");
        }

        running = false;
    }

}
