package com.skzh.aiProcess.domain;

import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class LockInfo {
    public boolean status;
    public FileLock fileLock;
    public RandomAccessFile lockFile;

    public LockInfo(boolean status, FileLock fileLock, RandomAccessFile lockFile) {
        this.status = status;
        this.fileLock = fileLock;
        this.lockFile = lockFile;
    }
}