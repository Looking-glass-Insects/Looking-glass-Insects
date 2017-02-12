package com.example.heyong.eeyeswindow.Cache;


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

/**
 * {@link #init(Context)}
 * <p>
 * start a new thread to cache
 * {@link #enqueue(WriteCallBack)}
 * <p>
 * get the cached data
 * {@link #getCache(ReadCallBack)}
 * <p>
 * get total size of cache
 * {@link #getSize()}
 * {@link #getFormatSize()}
 * <p>
 * clear all size
 * {@link #removeAll(IRemoveListener)}
 */

public class DiskLruCacheHelper {

    private static final long DEFAULT_SIZE = 10 * 1024 * 1024;//10M
    private static Context context;

    private DiskLruCacheHelper() {
    }

    public static void init(Context context) {
        DiskLruCacheHelper.context = context;
    }

    public static void assertContext() {
        if (context == null)
            throw new IllegalStateException("The context is null,call init() firstly");
    }

    /**
     * to start a new thread to write file
     *
     * @param callBack
     */
    public static void enqueue(@NonNull final WriteCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                workAtThread(callBack);
            }
        }).start();
    }

    public static void getCache(@NonNull ReadCallBack callBack) {
        assertContext();
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

    public static void removeCache(@NonNull RemoveCallBack callBack) {
        assertContext();
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(callBack.dir());
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, DEFAULT_SIZE);
            String key = hashKeyForDisk(callBack.key());
            mDiskLruCache.remove(key);
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all cache
     *
     * @param listener
     */
    public static void removeAll(@Nullable final IRemoveListener listener) {
        assertContext();
        final File cacheDir = getDiskCacheDir(null);// root
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                removeByFile(cacheDir);
                if (listener != null)
                    listener.onRemoveFin();
            }
        }).start();
    }

    public interface IRemoveListener {
        void onRemoveFin();//will call at a new thread
    }


    public static void removeByFile(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles())
                removeByFile(subFile);
        } else {
            file.delete();
        }
    }


    public static long getSize() {
        assertContext();
        File cacheDir = getDiskCacheDir(null);// root
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return getSizeByFile(cacheDir);
    }

    public static String getFormatSize() {
        return getFormatSize(getSize());
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


    private static void workAtThread(@NonNull WriteCallBack callBack) {
        assertContext();
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


    public abstract static class RemoveCallBack {
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
    }

    public abstract static class ReadCallBack {
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


        public abstract void onGetInputStream(InputStream is);
    }

    public abstract static class WriteCallBack {
        /**
         * get the dir name, if is null, will cache at cache/
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
}
