package cn.xiaolong.ticketsystem.base;

import android.app.Application;
import android.content.Context;

import com.kingja.loadsir.core.LoadSir;
import com.standards.library.app.AppContext;
import com.standards.library.app.ReturnCode;
import com.standards.library.app.ReturnCodeConfig;
import com.standards.library.cache.DataProvider;
import com.standards.library.network.NetworkConfig;
import com.standards.library.util.LogUtil;

import cn.jpush.android.api.JPushInterface;
import cn.xiaolong.ticketsystem.BuildConfig;
import cn.xiaolong.ticketsystem.inter.ErrorCallback;
import cn.xiaolong.ticketsystem.inter.LoadingCallback;


/**
 * <请描述这个类是干什么的>
 *
 * @data: 16/9/19 下午2:40
 * @version: V1.0
 */
public class App extends Application {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        initLoadView();
        sContext = getContext();
        LogUtil.init(BuildConfig.DEBUG_LOG, "lucky");
        DataProvider.init(this);
        NetworkConfig.setBaseUrl(BuildConfig.HOST_URL);
        ReturnCodeConfig.getInstance().initReturnCode(ReturnCode.CODE_SUCCESS, ReturnCode.CODE_EMPTY);
    }

    private void initLoadView() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

}
