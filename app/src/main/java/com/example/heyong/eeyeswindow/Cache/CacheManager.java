package com.example.heyong.eeyeswindow.Cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Heyong
 *
 * replaced by {@link DiskLruCacheHelper}
 */

@Deprecated
public class CacheManager {
    static String TAG = "CacheManager";

    /**
     *  缓存文件夹
     */
    public static String CACHE_OBJ = "object";




    private DiskLruCache cache;
    private String cachePath;
    private Context context;

    public CacheManager(Context context) {
        this.context = context;
        cache = null;
    }

    /**
     * 坑：我尝试新建线程执行，但奔溃
     *
     * @param uniqueName 文件夹目录
     * @param URL_OR_STR 最终文件名
     * @param content
     */
    public void startCache(final String uniqueName, final String URL_OR_STR, final Serializable content) {
        try {
            open(uniqueName);
            DiskLruCache.Editor editor = getEditor(URL_OR_STR);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                oos.writeObject(content);
                oos.flush();
                oos.close();
                editor.commit();
            }
            cache.flush();
            close();
        } catch (IOException e) {

        }
    }


    /**
     * 获取缓存
     *
     * @param uniqueName
     * @param key
     * @return
     */
    public Object getCache(String uniqueName, String key) {
        open(uniqueName);
        String _key = hashKeyForDisk(key);
        DiskLruCache.Snapshot snapShot = null;
        ObjectInputStream ois = null;
        try {
            snapShot = cache.get(_key);
            if (snapShot == null)
                return null;
            InputStream is = snapShot.getInputStream(0);
            ois = new ObjectInputStream(is);
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e) {
            return null;//
        }
        Object obj = null;
        try {
            obj = ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
        close();
        return obj;
    }


    /**
     * 清除缓存
     *
     * @param uniqueName
     * @param key
     * @return
     */
    public boolean clearCache(String uniqueName, String key) {
        open(uniqueName);
        String _key = hashKeyForDisk(key);
        try {
            cache.remove(_key);
        } catch (IOException e) {
            close();
            return false;
        }
        close();
        return true;
    }


    public void clearAllCache() {
        openRoot();
        try {
            cache.delete();
        } catch (IOException e) {
            close();
        }
        close();
    }

    public String getAllSize() {
        return getFormatSize(getAllLongSize());
    }

    public long getAllLongSize() {
        //openRoot();
        open(CACHE_OBJ);
        if (cache == null) {
            return 0;
        }
        long size = cache.size();
        close();
        return size;
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

    private void openRoot() {
        try {
            File cacheDir = getDiskCacheDir(context);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            cache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 200 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open(String uniqueName) {
        try {
            File cacheDir = getDiskCacheDir(context, uniqueName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            Log.e(TAG, cacheDir.getAbsolutePath());
            cache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 200 * 1024 * 1024);
            if (cache == null) {
                Log.e(TAG, "cache == null at open()");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {

            }
            cache = null;
        }
    }

    /**
     * editor.newOutputStream()方法来创建一个输出流
     */
    private DiskLruCache.Editor getEditor(String URL_OR_STR) {
        String key = hashKeyForDisk(URL_OR_STR);
        DiskLruCache.Editor editor = null;
        try {
            if (cache == null) {
                Log.e(TAG, "cache == null at getEditor()");
                return null;
            }
            editor = cache.edit(key);
        } catch (IOException e) {

        }
        return editor;
    }

    /**
     * 根地址
     *
     * @param context
     * @return
     */
    private File getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath);
    }

    /**
     * 获取缓存地址
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * app版本号
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * MD5算法将url转为唯一字符串
     *
     * @param key
     * @return
     */
    private String hashKeyForDisk(String key) {
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

    private String bytesToHexString(byte[] bytes) {
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


    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
