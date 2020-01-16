

package com.shopping.basket;

import android.content.Context;
//import android.support.multidex.MultiDex;
//import android.text.TextUtils;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//import com.danikula.videocache.HttpProxyCacheServer;
//import com.gnpc.travel.drink.food.gnpcclub.CustomView.LruBitmapCache;
//
//import static com.android.volley.VolleyLog.TAG;

/**
 * Stores general information for the application that can be accessed within any other class
 */
public class Application extends android.app.Application {
    private static Context appContext;
    public static Application instance;
//    private com.android.volley.toolbox.ImageLoader mImageLoader;
//    LruBitmapCache mLruBitmapCache;
//    private HttpProxyCacheServer proxy;
//    public static HttpProxyCacheServer getProxy(Context context) {
//        Application app = (Application) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }

//    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer.Builder(this)
//                .maxCacheSize(1024 * 1024)
//                .build();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appContext = getApplicationContext();
//        ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).imageDownloader(new BaseImageDownloader(this) {
//
//            @Override
//            protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
//                throw new IOException();
//            }
//        }).build();
//
    }
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
    public static Context getContext() {
        return appContext;
    }
    public static Application getContext1() {
        return instance;
    }

//    public static void notifyForegroundStateChanged(boolean inForeground) {
//        int old = mForegroundActivities;
//        if (inForeground) {
//            mForegroundActivities++;
//        } else {
//            mForegroundActivities--;
//        }
//
//        if (old == 0 || mForegroundActivities == 0) {
//            Intent intent = new Intent(appContext,MediaPlayerService.class);
//            intent.setAction(MediaPlayerService.CHANGE_FOREGROUND_STATE);
//            intent.putExtra(MediaPlayerService.NOW_IN_FOREGROUND, mForegroundActivities == 0);
//            appContext.startService(intent);
//        }
//
//    }



//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//
//        return mRequestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }
//    public com.android.volley.toolbox.ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            getLruBitmapCache();
//            mImageLoader = new com.android.volley.toolbox.ImageLoader(this.mRequestQueue, mLruBitmapCache);
//        }
//
//        return this.mImageLoader;
//    }
//    public LruBitmapCache getLruBitmapCache() {
//        if (mLruBitmapCache == null)
//            mLruBitmapCache = new LruBitmapCache();
//        return this.mLruBitmapCache;
//    }
    public  static synchronized Application getInstance(){
        return instance;
    }
}