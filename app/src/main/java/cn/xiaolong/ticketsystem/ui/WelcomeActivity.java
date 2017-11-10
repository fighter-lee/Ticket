package cn.xiaolong.ticketsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.standards.library.util.LogUtil;

import cn.xiaolong.ticketsystem.R;
import cn.xiaolong.ticketsystem.api.Dao;
import cn.xiaolong.ticketsystem.bean.CaiInfo;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WelcomeActivity extends AppCompatActivity {
    ImageView ivWelcome;
    private boolean isShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ivWelcome = (ImageView) findViewById(R.id.iv_welcome);
        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
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
                        LogUtil.d("onNext");
                        if ("1".equals(caiInfo.getData().getShow_url()) && !TextUtils.isEmpty(caiInfo.getData().getUrl())) {
                            isShow = true;
                        } else {
                            isShow = false;
                        }
                    }
                });

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        ivWelcome.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent;
                if (isShow){
                    intent = new Intent(WelcomeActivity.this, CaiActivity.class);
                }else{
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                }
                LogUtil.d("isshow:"+isShow);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                WelcomeActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
