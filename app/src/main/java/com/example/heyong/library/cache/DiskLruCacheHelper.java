package com.example.heyong.library.cache;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * {@link #init(Context)}
 * <p>
 * {@link #destroy()}
 * <p>
 * start to cache
 * {@link #writeCache(WriteCallBack)}
 * <p>
 * get the cached data
 * {@link #getCache(ReadCallBack)}
 * <p>
 * get total size of cache
 * {@link #getSize()}
 * {@link #getFormatSize(double)}
 * <p>
 * clear all size
 * {@link #removeAllCache(IRemoveListener)}
 */

public class DiskLruCacheHelper {

    private static final String TAG = "DiskLruCacheHelper";

    private static final long DEFAULT_SIZE = 10 * 1024 * 1024;//10M
    private static Context context;
    private static Queue<CallBack> workQueue = new ConcurrentLinkedQueue<>();
    private static boolean isRunning;


    private DiskLruCacheHelper() {
    }


    /**
     * to initialize the context as well as the work thread.
     * @param context
     */
    public static void init(Context context) {
        if (isRunning)
            throw new IllegalStateException("init() has been called");
        DiskLruCacheHelper.context = context;
        isRunning = true;
        new Thread(new WorkThread()).start();
    }

    /**
     * to finish the work thread.
     */
    public static void destroy() {
        isRunning = false;
    }

    /**
     * to see the work thread is running.
     * @return true if running.
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * to see the work queue if has work.
     * @return true if the size of the work queue is not 0.
     */
    public static boolean hasWork(){
        synchronized (workQueue){
            return workQueue.size() != 0;
        }
    }

    /**
     * write file
     *
     * @param callBack
     */
    public static void writeCache(@NonNull final WriteCallBack callBack) {
        synchronized (workQueue) {
            workQueue.add(callBack);
        }
    }

    /**
     * get the cached data
     *
     * @param callBack
     */
    public static void getCache(@NonNull ReadCallBack callBack) {
        synchronized (workQueue) {
            workQueue.add(callBack);
        }
    }

    /**
     * remove the specific file
     *
     * @param callBack
     */
    public static void removeCache(@NonNull RemoveCallBack callBack) {
        synchronized (workQueue) {
            workQueue.add(callBack);
        }
    }

    /**
     * clear all cache
     *
     * @param listener
     */
    public static void removeAllCache(@Nullable final IRemoveListener listener) {
        synchronized (workQueue) {
            workQueue.add(listener);
        }
    }

    /**
     * get the total size of the cache.
     * @param callBack
     */
    public static void getSize(SizeCallBack callBack) {
        synchronized (workQueue) {
            workQueue.add(callBack);
        }
    }


    static class WorkThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                if (!workQueue.isEmpty()) {
                    CallBack callBack = workQueue.remove();
                    if (callBack instanceof WriteCallBack) {
                        workAtThread((WriteCallBack) callBack);
                    } else if (callBack instanceof ReadCallBack) {
                        workAtThread((ReadCallBack) callBack);
                    } else if (callBack instanceof RemoveCallBack) {
                        workAtThread((RemoveCallBack) callBack);
                    } else if (callBack instanceof IRemoveListener) {
                        workAtThread((IRemoveListener) callBack);
                    } else if (callBack instanceof SizeCallBack) {
                        workAtThread((SizeCallBack) callBack);
                    }
                }
            }
        }

        private static void workAtThread(@NonNull WriteCallBack callBack) {
            DiskLruCache mDiskLruCache = null;
            try {
                File cacheDir = getDiskCacheDir(callBack.dir());
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, callBack.maxSize() > 0 ? callBack.maxSize() : DEFAULT_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String key = hashKeyForDisk(callBack.key());
            try {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (callBack.onGetStream(outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void workAtThread(@NonNull ReadCallBack callBack) {
            DiskLruCache mDiskLruCache = null;
            try {
                File cacheDir = getDiskCacheDir(callBack.dir());
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, DEFAULT_SIZE);
                String key = hashKeyForDisk(callBack.key());
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                if (snapShot != null) {
                    InputStream is = snapShot.getInputStream(0);
                    callBack.onGetInputStream(is);
                }
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void workAtThread(@NonNull RemoveCallBack callBack) {
            DiskLruCache mDiskLruCache = null;
            try {
                File cacheDir = getDiskCacheDir(callBack.dir());
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, DEFAULT_SIZE);
                String key = hashKeyForDisk(callBack.key());
                callBack.beforeRemove();
                mDiskLruCache.remove(key);
                mDiskLruCache.close();
                callBack.afterRemove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void workAtThread(@Nullable IRemoveListener listener) {
            final File cacheDir = getDiskCacheDir(null);// root
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            removeByFile(cacheDir);
            if (listener != null)
                listener.onRemoveFin();
        }

        private static void workAtThread(@NonNull SizeCallBack callBack) {
            long size = getSize();
            String formatSize = getFormatSize(size);
            callBack.onGetSize(size);
            callBack.onGetSize(formatSize);
        }
    }

    public static interface CallBack {
    }

    public abstract static class RemoveCallBack implements CallBack {
        /**
         * get the dir name, if is null, will cache at cache/
         *
         * @return the dir name
         */
        public abstract String dir();

        /**
         * the unique key
         *
         * @return key
         */
        public abstract String key();

        public void afterRemove(){}

        public void beforeRemove(){}
    }

    public abstract static class ReadCallBack implements CallBack {
        /**
         * get the dir name, if is null, will cache at /cache/
         *
         * @return the dir name
         */
        public abstract String dir();

        /**
         * the unique key
         *
         * @return key
         */
        public abstract String key();

        /**
         *
         * @param is the input stream of the file
         */
        public abstract void onGetInputStream(InputStream is);
    }

    public abstract static class WriteCallBack implements CallBack {
        /**
         * get the dir name, if is null, will cache at /cache/
         *
         * @return the dir name
         */
        public abstract String dir();

        /**
         * the unique key,cannot be null or ""
         *
         * @return key
         */
        public abstract String key();

        /**
         * the max size of the file,if <=0,will be {@value DEFAULT_SIZE}
         *
         * @return max size
         */
        public abstract long maxSize();

        /**
         * use the outputStream to write file
         *
         * @param os
         * @return true if cache
         */
        public abstract boolean onGetStream(OutputStream os);
    }

    public abstract static class SizeCallBack implements CallBack {
        public void onGetSize(long size) {
        }

        public void onGetSize(String size) {
        }
    }

    public interface IRemoveListener extends CallBack {
        void onRemoveFin();
    }


    /**
     * /sdcard/Android/data/<application package>/cache
     * /data/data/<application package>/cache
     *
     * @param uniqueName your dir name
     * @return
     */
    private static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (uniqueName == null)
            uniqueName = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private static int getAppVersion() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static String hashKeyForDisk(String key) {
        if (key == null)
            throw new IllegalStateException("The key cannot be null");
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private static long getSize() {
        File cacheDir = getDiskCacheDir(null);// root
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        try {
            return getSizeByFile(cacheDir);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * to remove all files under the dir(except the dir).
     * @param file the dir
     */
    private static void removeByFile(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles())
                removeByFile(subFile);
        } else {
            file.delete();
        }
    }


    /**
     * to get all size
     *
     * @param file that should be a dir
     * @return size
     */
    private static long getSizeByFile(File file) {
        DiskLruCache mDiskLruCache = null;
        long size = 0;
        if (file.isDirectory()) {
            try {
                mDiskLruCache = DiskLruCache.open(file, getAppVersion(), 1, DEFAULT_SIZE);
                size += mDiskLruCache.size();
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (File subFile : file.listFiles()) {
                size += getSizeByFile(subFile);
            }
        } else {
            return 0;
        }
        return size;
    }
}
