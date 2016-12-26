package com.example.heyong.eeyeswindow.Cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Heyong
 */

public class CacheManager {

    private  DiskLruCache cache;
    private String cachePath;
    private Context context;

    public CacheManager(Context context) {
        this.context = context;
    }

    /**
     *
     * @param uniqueName 文件夹目录
     * @param URL_OR_STR  最终文件名
     * @param content
     * @param subscriber
     */
    public void startCache(final String uniqueName, final String URL_OR_STR, final Serializable content, @Nullable Subscriber<? super String> subscriber){
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
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
                            sub.onNext(cachePath);
                            sub.onCompleted();
                        } catch (IOException e) {
                            try {
                                throw new IOException("io exception");
                            } catch (IOException e1) {
                            }
                        }
                    }
                }
        );
        if (subscriber == null){
            myObservable.subscribe();
        }else{
            myObservable.subscribe(subscriber);
        }
    }
    public void startCache(final String uniqueName, final String URL, Subscriber<? super String> subscriber){
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        try {
                            open(uniqueName);
                            DiskLruCache.Editor editor = getEditor(URL);
                            if (editor != null) {
                                OutputStream outputStream = editor.newOutputStream(0);
                                if (downloadUrlToStream(URL, outputStream)) {
                                    editor.commit();
                                } else {
                                    editor.abort();
                                }
                            }
                            cache.flush();
                            sub.onNext(cachePath);
                            sub.onCompleted();
                        } catch (IOException e) {
                            sub.onError(e);
                        }
                    }
                }
        );
        myObservable.subscribe(subscriber);
    }


    public Object getCache(String uniqueName ,String key){
        open(uniqueName);
        String _key = hashKeyForDisk(key);
        DiskLruCache.Snapshot snapShot = null;
        ObjectInputStream ois = null;
        try {
            snapShot = cache.get(_key);
            if(snapShot == null)
                return null;
            InputStream is = snapShot.getInputStream(0);
            ois = new ObjectInputStream(is);
        } catch (IOException e) {
            return null;
        }
        Object obj = null;
        try {
            obj = ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
        return obj;
    }

    private void open(String uniqueName){
        try {
            File cacheDir = getDiskCacheDir(context, uniqueName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            cache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * editor.newOutputStream()方法来创建一个输出流
     */
    public DiskLruCache.Editor getEditor(String URL_OR_STR){
        String key = hashKeyForDisk(URL_OR_STR);
        DiskLruCache.Editor editor = null;
        try {
            editor = cache.edit(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editor;
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
