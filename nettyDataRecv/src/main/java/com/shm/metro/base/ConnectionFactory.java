package com.shm.metro.base;

import org.apache.commons.pool2.PooledObjectFactory;

import java.io.Serializable;

public abstract interface ConnectionFactory<T>
  extends PooledObjectFactory<T>, Serializable
{
  public abstract T createConnection()
    throws Exception;
}

