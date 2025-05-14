package org.tron.core.services;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.core.ChainBaseManager;
import org.tron.core.db.Manager;
import org.tron.core.db.RevokingDatabase;
import org.tron.core.db2.core.Chainbase;
import org.tron.core.store.DynamicPropertiesStore;

@Slf4j(topic = "API")
public abstract class WalletOnCursor {

  protected Chainbase.Cursor cursor = Chainbase.Cursor.HEAD;
  @Autowired
  private Manager dbManager;

  @Autowired
  private RevokingDatabase revokingStore;

  public <T> T futureGet(TronCallable<T> callable) {
    try {
      dbManager.setCursor(cursor);
      return callable.call();
    } finally {
      dbManager.resetCursor();
    }
  }

  public void futureGet(Runnable runnable) {
    try {
      dbManager.setCursor(cursor);
      runnable.run();
    } finally {
      dbManager.resetCursor();
    }
  }

  public interface TronCallable<T> extends Callable<T> {

    @Override
    T call();
  }

  @FunctionalInterface
  public interface ExceptionalRunnable {
    void run(boolean statedMayChanged) throws Exception;
  }

  public void futureGet(ExceptionalRunnable runnable, Long specifiedNumber) throws Exception  {
    DynamicPropertiesStore dynamicPropertiesStore = dbManager.getChainBaseManager().getDynamicPropertiesStore();
    logger.info("futureGet specifiedNumber = {}, latest header number = {}, latest solidity number = {}", specifiedNumber, dynamicPropertiesStore.getLatestBlockHeaderNumber(), dynamicPropertiesStore.getLatestSolidifiedBlockNum());
    if (specifiedNumber == null) {
      // TODO implement me
      runnable.run(false);
      return;
    }

    try {
      ChainBaseManager chainBaseManager = dbManager.getChainBaseManager();
      Long snapshotVersion = chainBaseManager.getDynamicPropertiesStore().getSpecifiedNumberSnapshotVersion(specifiedNumber);
      if (snapshotVersion == null) {
        logger.warn("Snapshot version is null for specified number: {}, use head", specifiedNumber);
      }
      // can be null
      revokingStore.setSpecifiedCursor(snapshotVersion);

      runnable.run(!revokingStore.hasCommitted());
    } finally {
      dbManager.resetCursor();
    }
  }

}
