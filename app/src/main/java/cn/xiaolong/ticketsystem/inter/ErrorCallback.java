package cn.xiaolong.ticketsystem.inter;


import com.kingja.loadsir.callback.Callback;

import cn.xiaolong.ticketsystem.R;


public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
}
