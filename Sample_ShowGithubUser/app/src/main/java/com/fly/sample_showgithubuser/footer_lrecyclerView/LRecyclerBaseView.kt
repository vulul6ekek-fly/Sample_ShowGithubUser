package com.fly.sample_showgithubuser.footer_lrecyclerView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

open class LRecyclerBaseView : RecyclerView {

    private var mAdapter: LRecyclerAdapterDesign? = null
    private val mDataObserver: AdapterDataObserver = DataObserver()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setAdapter(adapter: Adapter<*>?) {
        mAdapter?.getInnerAdapter()?.unregisterAdapterDataObserver(mDataObserver)
        mAdapter = adapter as LRecyclerAdapterDesign
        super.setAdapter(mAdapter)
        mAdapter?.getInnerAdapter()?.registerAdapterDataObserver(mDataObserver)
        mDataObserver.onChanged()
    }

    fun destroyHeaderView(){
        mAdapter?.destroy()
    }

    private inner class DataObserver : AdapterDataObserver() {

        override fun onChanged() {
            mAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mAdapter?.let {
                it.notifyItemRangeRemoved(positionStart + it.getHeaderViewsCount(), itemCount)
            }
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mAdapter?.let {
                val headerCount = it.getHeaderViewsCount()
                it.notifyItemRangeChanged(fromPosition + headerCount, toPosition + headerCount + itemCount)
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mAdapter?.let {
                it.notifyItemRangeInserted(positionStart + it.getHeaderViewsCount(), itemCount)
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mAdapter?.let {
                it.notifyItemRangeChanged(positionStart + it.getHeaderViewsCount(), itemCount)
            }
        }
    }
}