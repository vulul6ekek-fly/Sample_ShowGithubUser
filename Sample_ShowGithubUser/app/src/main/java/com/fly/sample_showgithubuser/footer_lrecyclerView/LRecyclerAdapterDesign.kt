package com.fly.sample_showgithubuser.footer_lrecyclerView

import androidx.recyclerview.widget.RecyclerView

abstract class LRecyclerAdapterDesign: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * RecyclerView使用的，真正的Adapter
     */
    protected var mInnerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    constructor(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : super(){
        mInnerAdapter = adapter
    }

    fun getInnerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return mInnerAdapter
    }

    abstract fun destroy()

    abstract fun getHeaderViewsCount(): Int

}