package com.shm.metro.base;

import java.io.Serializable;

public abstract interface ConnectionPool<T>
  extends Serializable
{
  public abstract T getConnection();
  
  public abstract void returnConnection(T paramT);
  
  public abstract void invalidateConnection(T paramT);
}
