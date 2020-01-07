package com.cysion.ktbox.base

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cysion.ktbox.bean.StateEvent
import com.cysion.ktbox.listener.IRefreshListener
import com.cysion.wedialog.WeDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

abstract class BaseModelActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: VM
    protected var mIRefreshListener: IRefreshListener? = null
    //避开与某些闭包内的this冲突
    protected val self: FragmentActivity by lazy {
        this
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        viewModel = createViewModel()
        mIRefreshListener = getRefreshListenerOrNull()
        viewModel.observeState(this, Observer {
            when (it.type) {
                StateEvent.LOADING -> startLoading(it.msg)
                StateEvent.LOADED -> stopLoading()
                StateEvent.REFRESH_OK -> mIRefreshListener?.onRefreshOk(it.msg)
                StateEvent.REFRESH_FAIL -> mIRefreshListener?.onRefreshFail(it.msg)
                StateEvent.LOAD_MORE_OK -> mIRefreshListener?.onLoadMoreOk(it.msg)
                StateEvent.LOAD_MORE_FAIL -> mIRefreshListener?.onLoadMoreFail(it.msg)
                else -> onStateEventChanged(it.type, it.msg?:"")
            }
        })
        EventBus.getDefault().register(this)
        initView()
        observeModel()
        initData()
    }

    fun startLoading(msg: String?) {
        WeDialog.loading(this)
    }

    fun stopLoading() {
        WeDialog.dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun fromEventbus(app: Application) {
        //仅占位，防止子类继承出问题
        //子类可以仿照这种写法，但是事件类型必须是自定义的
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    protected abstract fun getLayoutId(): Int

    @Suppress("UNCHECKED_CAST")
    protected open  fun createViewModel(): VM{
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProviders.of(this).get(tClass) as VM
        }
        return viewModel
    }
    protected abstract fun initView()

    protected abstract fun observeModel()

    protected open fun initData(){}

    protected abstract fun getRefreshListenerOrNull(): IRefreshListener?

    protected abstract fun onStateEventChanged(type: Int, msg: String)
}