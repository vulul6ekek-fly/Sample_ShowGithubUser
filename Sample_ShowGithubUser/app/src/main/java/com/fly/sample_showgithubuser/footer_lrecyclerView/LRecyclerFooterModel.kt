package com.fly.sample_showgithubuser.footer_lrecyclerView

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fly.sample_showgithubuser.R

class LRecyclerFooterModel {

    private var lRecyclerFooterAction: LRecyclerFooterAction
    private lateinit var recyclerView: LRecyclerBaseView
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var lRecyclerFooterAdapter: LRecyclerFooterAdapter

    private var pastVisiblesItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var loading = true
    //是否有更多資料
    private var isHasMore = true
    private var startY: Float = 0F
    private var startX: Float = 0F

    companion object{
        private const val HIDE_THRESHOLD = 20
    }

    constructor(activity: Activity, lRecyclerFooterAction: LRecyclerFooterAction) {
        this.lRecyclerFooterAction = lRecyclerFooterAction
        initView(activity)
        setView(activity)
    }

    fun addHeaderView(view: View){
        lRecyclerFooterAdapter.addHeaderView(view)
    }

    fun getInnerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return lRecyclerFooterAdapter.getInnerAdapter()
    }

    fun updateList(hasMore: Boolean) {
        isHasMore = hasMore
        lRecyclerFooterAdapter.updateList(hasMore)
        loading = true
    }

    fun closeLoading() {
        lRecyclerFooterAdapter.closeFooter()
        loading = true
    }

    fun destroyHeaderView() {
        recyclerView.destroyHeaderView()
    }

    fun releaseResources(){
        destroyHeaderView()
        lRecyclerFooterAdapter.clearHandlerMessage()
    }

    fun getItemCount(): Int = lRecyclerFooterAdapter.itemCount

    fun isHasMore(): Boolean = isHasMore

    private val touchListener = object : View.OnTouchListener {

        private var isShowFooter = false

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            val action = event?.getAction()
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.getY()
                    startX = event.getX()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (loading) {
                        if (checkIsInTheEnd() && !isHasMore && !checkIsOnScreen()) {
                            val endY = event.getY()
                            val endX = event.getX()
                            val distanceX = Math.abs(endX - startX)
                            val distanceY = Math.abs(endY - startY)
                            if (distanceY > HIDE_THRESHOLD && !isShowFooter) {
                                isShowFooter = true
                                lRecyclerFooterAdapter.updateFooter(isHasMore)
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> if (isShowFooter) {
                    isShowFooter = false
                }
            }
            return false
        }
    }

    private fun setView(activity: Activity) {
        lRecyclerFooterAdapter = LRecyclerFooterAdapter(lRecyclerFooterAction.initInnerAdapter())
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setAdapter(lRecyclerFooterAdapter)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (loading) {
                    //擋住連續觸發
                    if (checkIsInTheEnd()) {
                        //判斷是否在底部
                        loading = false
                        if (isHasMore) {
                            lRecyclerFooterAdapter.updateFooter(isHasMore,recyclerView)
                            //主要是做顯示加載的文字
                        }
                        lRecyclerFooterAction.getMoreData()
                    }
                }
            }
        })
        recyclerView.setOnTouchListener(touchListener)
    }

    /* 所有資料數顯示不超過螢幕可視範圍,就不需要顯示FooterView */
    private fun checkIsOnScreen(): Boolean{
        return mLayoutManager.findFirstVisibleItemPosition() == 0
    }

    private fun checkIsInTheEnd(): Boolean {
        visibleItemCount = mLayoutManager.childCount
        totalItemCount = mLayoutManager.itemCount
        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
        return visibleItemCount + pastVisiblesItems >= totalItemCount
    }

    private fun initView(activity: Activity) {
        recyclerView = activity.findViewById(R.id.recyclerView)
    }

    interface LRecyclerFooterAction{
        fun getMoreData()
        fun initInnerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}