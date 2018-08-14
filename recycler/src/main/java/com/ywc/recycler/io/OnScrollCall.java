package com.ywc.recycler.io;

import com.ywc.recycler.mode.LoadMode;
import com.ywc.recycler.mode.ScrollMode;

/**
 * Created by Administrator on 2018/7/18.
 */

public interface OnScrollCall {
    void callback(LoadMode loadMode);
}
