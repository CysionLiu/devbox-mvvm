package com.cysion.ktbox.base

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cysion.ktbox.bean.StateEvent
import com.cysion.ktbox.listener.IRefreshListener
import com.cysion.ktbox.utils.logd
import com.cysion.wedialog.WeDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

abstract class BaseModelFragment<VM : BaseViewModel>: Fragment() {

    protected lateinit var viewModel: VM
    protected var mIRefreshListener: IRefreshListener? = null
    private var mEverLoaded = false
    //在onCreateView初始化；可避免getActivity的空指针
    val context by lazy {
        getActivity()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(getLayoutId(),container,false)
        viewModel = createViewModel()
        EventBus.getDefault().register(this)
        context.hasWindowFocus()//此时初始化context指向activity；可避免getActivity的空指针，即使fragment被回收
        mIRefreshListener = getRefreshListenerOrNull()
        viewModel.observeState(this, Observer {
            when (it.type) {
                StateEvent.LOADING -> startLoading(it.msg)
                StateEvent.LOADED -> stopLoading()
                StateEvent.REFRESH_OK -> mIRefreshListener?.onRefreshOk(it.msg)
                StateEvent.REFRESH_FAIL -> mIRefreshListener?.onRefreshFail(it.msg)
                StateEvent.LOAD_MORE_OK -> mIRefreshListener?.onLoadMoreOk(it.msg)
                StateEvent.LOAD_MORE_FAIL -> mIRefreshListener?.onLoadMoreFail(it.msg)
                else -> onReceivedStateEvent(it.type, it.msg?:"")
            }
        })
        return root
    }

    protected fun stopLoading(){
        WeDialog.dismiss()
    }

    protected fun startLoading(msg: String?){
        WeDialog.loading(context)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeModel()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    //仅用来声明默认接收方法的，实际不应该接收任何消息；子类仿照该方法，自定义事件即可
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun fromEventBus(app: Application) {
    }

    //适用于viewpager添加fragment
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !mEverLoaded) {
            //首次可见，还未加载过数据
            mEverLoaded = true
            lazyLoad()
        } else if (isVisibleToUser) {
            visibleAgain()
        } else {
            hideAgain()
        }
    }

    //首次可见，还未加载过数据
    protected open fun lazyLoad() {
        logd("懒加载-->" + javaClass.simpleName)
    }

    //再次可见
    protected open fun visibleAgain() {
        logd("再次可见-->" + javaClass.simpleName)
    }

    protected open fun hideAgain() {
        logd("再次隐藏-->" + javaClass.simpleName)
    }

    //针对add方式添加的fragment
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //为方便调试找寻页面添加,add方式添加
        if (!hidden) {
            logd("可见--->" + javaClass.simpleName)
        } else {
            logd("不可见--->" + javaClass.simpleName)
        }
    }

    protected open fun initData() {
    }

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
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun observeModel()

    protected abstract fun onReceivedStateEvent(type: Int, msg: String)

    /*页面如果有刷新或者上拉加载操作，则不能为null*/
    protected open fun getRefreshListenerOrNull(): IRefreshListener?=null

}