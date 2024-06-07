package com.shm.metro.registry;

/**
 * ZooKeeper constant
 *
 * @author huangyong
 */
public interface Constant {

    String ZK_QUORUM = "zk_quorum";

    String ZK_SESSION_TIMEOUT = "zk_session_timeout";
    String ZK_SESSION_TIMEOUT_DEFAULT = "600000";

    String ZK_REGISTRY_PATH = "zk_registry_path";
    String ZK_REGISTRY_PATH_DEFAULT = "/registry";

    String ZK_DATA_PATH = "zk_data_path";
    String ZK_DATA_PATH_DEFAULT = "/data";
}
