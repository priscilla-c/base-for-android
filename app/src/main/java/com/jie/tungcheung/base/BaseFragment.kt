package com.jie.tungcheung.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jie.tungcheung.utils.binding.Binding
import com.jie.tungcheung.utils.binding.ViewDataBindRef
import com.jie.tungcheung.utils.kInject.core.initScope
import com.jie.tungcheung.utils.kInject.lifecyleowner.module.lifeModule
import java.lang.reflect.ParameterizedType



abstract class BaseFragment<V : ViewBinding, VM : AndroidViewModel> : Fragment() {

    protected var rootView: View? = null
    var binding: V by ViewDataBindRef()
    private var savedInstanceState: Bundle? = null

    val viewModel :VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(requireActivity().viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        if (arguments != null) {
            BundleAutoInject.inject(this)
            onBundle(requireArguments())
        }


        initScope {
            module(viewModel,lifeModule {
                scopeByName(viewModel.toString()) { requireActivity() } })
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = setContentView(inflater, container, getViewId())

        view?.apply {
            Binding.getBinding<V>(this@BaseFragment,view.rootView,0)?.apply {
                binding = this
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerUI()
        init(savedInstanceState)
        initListener()
    }



    inline fun <reified VM : ViewModel> createViewModel(bindActivity: Boolean = true): VM {
        return if (bindActivity) {
            activityViewModels<VM> { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }.value
        } else {
            return viewModels<VM> { ViewModelProvider.NewInstanceFactory() }.value
        }
    }


    /**
     * fragment??????????????????????????????onResume???????????????????????????????????????????????????
     */
    open fun onVisible() {


    }

    /**
     * fragment????????????????????????,onPause?????????,??????????????????????????????
     */
    open fun onHidden() {

    }

    inline fun <reified T> startActivity(bundle: Bundle? = null,transition: Boolean = false) {
        val intent = Intent(context, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent,if(transition){
            ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
        }else{
            null
        })
    }


    override fun onResume() {//???activity???onResume?????????Fragment???????????????????????????????????????fragment???hide???visible??????????????????????????????
        super.onResume()
        if (isAdded && !isHidden) {//???isVisible?????????false?????????mView.getWindowToken???null
            onVisible()
        }
    }

    override fun onPause() {
        if (isVisible)
            onHidden()
        super.onPause()
    }



    //??????fragment???????????????????????????????????????????????????????????????????????????????????????????????????????????????onResume???onPause????????????????????????
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onHidden()
        }
    }


    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?, resId: Int): View? {
        if (rootView == null) {
            rootView = inflater.inflate(resId, container, false)
        } else {
            container?.removeView(rootView)
        }
        return rootView
    }


    //abstract fun initViewModel(): VM

    abstract fun getViewId(): Int

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()


    fun binding(block:V.()->Unit){
        block.invoke(binding)
    }

}