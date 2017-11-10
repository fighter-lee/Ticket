package cn.xiaolong.ticketsystem.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import cn.xiaolong.ticketsystem.R;

/**
 * Created by fighter_lee on 2017/8/8.
 */

public abstract class BaseActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            initView(savedInstanceState);
        }
        initSystemBar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取布局ID
     *
     * @return 布局id
     */
    protected abstract int getContentViewLayoutID();


    private void initSystemBar() {
        if (translucentStatusBar()) {
            if (null == getToolbar()) {
                return;
            }
            return;
        }
        if (tintStatusBar()) {
            return;
        }

    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }

    /**
     * 着色状态栏
     *
     * @return
     */
    protected boolean tintStatusBar() {
        return true;
    }

    /**
     * 获取toolbar
     *
     * @return
     */
    protected View getToolbar() {
        return null;
    }

    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }
}
