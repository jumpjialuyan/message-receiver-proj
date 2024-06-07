package com.shm.metro.observer;

import com.shm.metro.enums.NotifyType;

/**
 * Created by Jerry on 2023/12/25.
 */
public interface Observer {

    void update(NotifyType notifyType);
}
