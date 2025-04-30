package org.tron.core.db2.core;

import org.tron.common.utils.Time;

import java.util.concurrent.atomic.AtomicLong;

public class SnapshotVersion {
  private final AtomicLong currentVersion;


  public SnapshotVersion() {
    long defaultVersion = Time.getCurrentMillis();
    this.currentVersion = new AtomicLong(defaultVersion);
  }

  public long getCurrentVersion() {
    return currentVersion.get();
  }

  public long addOne() {
    return currentVersion.incrementAndGet();
  }

  private static class SingletonHolder {
    private static final SnapshotVersion INSTANCE = new SnapshotVersion();
  }

  public static SnapshotVersion getInstance() {
    return SingletonHolder.INSTANCE;
  }
}
