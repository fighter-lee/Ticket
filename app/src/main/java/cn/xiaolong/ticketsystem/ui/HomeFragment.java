package cn.xiaolong.ticketsystem.ui;//package cn.xiaolong.ticketsystem.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.WebDefaultSettingsManager;
import com.standards.library.util.LogUtil;

import cn.xiaolong.ticketsystem.R;
import cn.xiaolong.ticketsystem.api.Dao;
import cn.xiaolong.ticketsystem.base.WebviewFragment;
import cn.xiaolong.ticketsystem.bean.CaiInfo;
import cn.xiaolong.ticketsystem.inter.ErrorCallback;
import cn.xiaolong.ticketsystem.inter.LoadingCallback;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends WebviewFragment {
    private static final String TAG = "HomeFragment";
    protected AgentWeb.PreAgentWeb mAgentWeb;
    //    @BindView(R.id.rl_webview2)
    RelativeLayout rlWebview;
    private AgentWeb go;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_webview2;
    }

    @Override
    protected void init(View view) {
        rlWebview = (RelativeLayout) view.findViewById(R.id.rl_webview2);
        setWebView(view);
        request();
    }

    private void request() {
        Dao.getApiService()
                .getCaiInfo("http://app.412988.com/Lottery_server/check_and_get_url.php?type=android&appid=2017103110")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CaiInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CaiInfo caiInfo) {
                        if ("1".equals(caiInfo.getData().getShow_url()) && !TextUtils.isEmpty(caiInfo.getData().getUrl())) {
                            loadWebView(caiInfo.getData().getUrl());
                        } else {
                            loadWebView("http://www.500.com/");
                        }
                    }
                });

    }

    private void loadWebView(final String url) {
        //加载需要显示的网页
        //设置Web视图
        LogUtil.d(TAG, "loadWebView() url:" + url);
        go = mAgentWeb.go(url);
        go.getWebCreator().get().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }

    private void setWebView(View view) {
        LogUtil.d("webview:" + (rlWebview == null));
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(rlWebview, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件
                .setIndicatorColorWithHeight(-1, 2)//设置进度条颜色与高度-1为默认值，2单位为dp
                .setAgentWebWebSettings(getSettings())//设置 AgentWebSettings
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与WebView 一样
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                //                .setSecurityType(AgentWeb.SecurityType.strict) //严格模式
                .openParallelDownload()//打开并行下载 , 默认串行下载
                .setNotifyIcon(R.mipmap.download)
                .createAgentWeb()//创建AgentWeb
                .ready();//设置 WebSettings
    }

    @Override
    protected void onNetReload() {
        Log.d(TAG, "onNetReload: ");
        showLoadingUI();
        request();
    }

    private void showLoadingUI() {
        Log.d(TAG, "showLoadingUI() ");
        rlWebview.setVisibility(View.INVISIBLE);
        mBaseLoadService.showCallback(LoadingCallback.class);
    }

    private void showSuccessUI() {
        Log.d(TAG, "showSuccessUI() ");
        rlWebview.setVisibility(View.VISIBLE);
        mBaseLoadService.showSuccess();
    }

    private void showErrorUI() {
        Log.d(TAG, "showErrorUI() ");
        rlWebview.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBaseLoadService.showCallback(ErrorCallback.class);
            }
        }, 500);

    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG, "onProgressChanged() " + newProgress);
            if (newProgress < 60) {
            } else if (newProgress == 100) {
                showSuccessUI();
            } else if (newProgress >= 60) {
                showSuccessUI();
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(TAG, "onReceivedError() 1");
            showErrorUI();
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "onReceivedError() 2");
            showErrorUI();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    public AgentWebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        if (go != null) {
            return go.handleKeyEvent(keyCode, event);
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        if (go != null) {
            go.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (go != null) {
            go.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

}
