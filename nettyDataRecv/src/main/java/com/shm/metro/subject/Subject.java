package com.shm.metro.subject;/**
 * Created by Jerry on 2023/12/25.
 */

import com.shm.metro.enums.NotifyType;
import com.shm.metro.observer.Observer;

/**
 * 消息发布者
 *
 * @author Jerry
 * @create 2023-12-25 上午9:25
 **/

public interface Subject {
    /**
     * 增加订阅者
     * @param observer
     */
    public void attach(Observer observer);
    /**
     * 删除订阅者
     * @param observer
     */
    public void detach(Observer observer);
    /**
     * 通知订阅者更新消息
     */
    public void notify(NotifyType message);
}