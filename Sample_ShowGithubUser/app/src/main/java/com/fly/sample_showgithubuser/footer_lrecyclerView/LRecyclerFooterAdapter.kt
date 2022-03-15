package com.fly.sample_showgithubuser.footer_lrecyclerView

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fly.sample_showgithubuser.R
import java.util.*

class LRecyclerFooterAdapter(adapter: Adapter<RecyclerView.ViewHolder>) : LRecyclerAdapterDesign(adapter) {

    private var mHeaderViews: ArrayList<View>? = ArrayList<View>()
    private val mHeaderTypes: ArrayList<Int> = ArrayList()
    private var mSpanSizeLookup: SpanSizeLookup? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private var footHolder: FootHolder? = null
    private var hasMore = true
    private var fadeTips = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if (isHeaderType(viewType)) {
             return ViewHolder(
                 getHeaderViewByType(viewType)
             )
        }else if(viewType == TYPE_FOOTER_VIEW) {
             footHolder = FootHolder(LayoutInflater.from(parent.context).inflate(R.layout.footview, parent, false))
             return footHolder as FootHolder
         }else{
             return mInnerAdapter.onCreateViewHolder(parent, viewType)
         }
    }

    override fun getItemCount(): Int {
        return if (mInnerAdapter != null) {
            getHeaderViewsCount() + mInnerAdapter.itemCount + getFooterViewsCount()
        } else {
            getHeaderViewsCount() + getFooterViewsCount()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeader(position)) return
        if (holder is FootHolder) {
            handleFootHolder(holder)
        }else{
            val adjPosition: Int = position - getHeaderViewsCount()
            val adapterCount: Int
            if (mInnerAdapter != null) {
                adapterCount = mInnerAdapter.itemCount
                if (adjPosition < adapterCount) {
                    mInnerAdapter.onBindViewHolder(holder, adjPosition)
                    mOnItemClickListener?.let {
                        holder.itemView.setOnClickListener {
                            mOnItemClickListener!!.onItemClick(
                                holder.itemView,
                                adjPosition
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (isHeader(position)) return
            if (holder is FootHolder) {
                handleFootHolder(holder)
            }else{
                val adjPosition: Int = position - getHeaderViewsCount()
                val adapterCount: Int
                if (mInnerAdapter != null) {
                    adapterCount = mInnerAdapter.itemCount
                    if (adjPosition < adapterCount) {
                        mInnerAdapter.onBindViewHolder(holder, adjPosition, payloads)
                    }
                }
            }
        }
    }

    private fun handleFootHolder(holder: FootHolder){
        if (hasMore == true) {
            if (mInnerAdapter.itemCount > 0) {
                holder.tips.text = "正在載入更多..."
                if (fadeTips) {
                    holder.tips.visibility = View.VISIBLE
                }
            }
        } else {
            if (mInnerAdapter.itemCount > 0) {
                holder.tips.text = "沒有更多資料了"
                if (fadeTips) {
                    holder.tips.visibility = View.VISIBLE
                }
                mHandler.postDelayed({ holder.tips.visibility = View.GONE }, 500)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val adjPosition: Int = position - getHeaderViewsCount()
        if (isHeader(position)) {
            return mHeaderTypes[position]
        }
        if (isFooter(position)) {
            return TYPE_FOOTER_VIEW
        }
        val adapterCount: Int
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.itemCount
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemViewType(adjPosition)
            }
        }
        return TYPE_NORMAL
    }

    override fun getItemId(position: Int): Long {
        if (mInnerAdapter != null && position >= getHeaderViewsCount()) {
            var adjPosition = position - getHeaderViewsCount()

            if (hasStableIds()) {
                adjPosition--
            }
            val adapterCount = mInnerAdapter.itemCount
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemId(adjPosition)
            }
        }
        return -1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mSpanSizeLookup == null) {
                        if (isHeader(position) || isFooter(position)) manager.getSpanCount() else 1
                    } else {
                        if (isHeader(position) || isFooter(position)) manager.getSpanCount() else mSpanSizeLookup!!.getSpanSize(
                            manager,
                            position - getHeaderViewsCount()
                        )
                    }
                }
            })
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            if (isHeader(holder.layoutPosition) || isFooter(holder.layoutPosition)) {
                lp.setFullSpan(true)
            }
        }
        mInnerAdapter.onViewAttachedToWindow(holder)
    }

    fun updateList(hasMore: Boolean) {
        fadeTips = false
        this.hasMore = hasMore
        notifyDataSetChanged()
    }

    fun closeFooter(){
        footHolder?.tips?.visibility = View.GONE
    }

    fun updateFooter(hasMore: Boolean,recyclerView: RecyclerView? = null) {
        fadeTips = true
        this.hasMore = hasMore
        if(hasMore){
            notifyItemChanged(itemCount - 1)
            recyclerView?.let { scrollToBottom(it) }
        }else{
            notifyDataSetChanged()
        }
    }

    private fun scrollToBottom(recyclerView: RecyclerView){
        mHandler.postDelayed({
            recyclerView.scrollToPosition(itemCount - 1)
        },30)
    }

    fun isFooter(position: Int): Boolean {
        return position == itemCount - 1
    }

    fun getFooterViewsCount(): Int {
        return 1
    }

    fun addHeaderView(view: View) {
        mHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews?.size!!)
        mHeaderViews?.add(view)
    }

    fun removeHeaderView() {
        if (getHeaderViewsCount() > 0) {
            val headerView = getHeaderView()
            mHeaderViews?.remove(headerView)
            notifyDataSetChanged()
        }
    }

    override fun getHeaderViewsCount(): Int {
        return mHeaderViews?.size?:0
    }

    fun isHeader(position: Int): Boolean {
        return position >= 0 && position < mHeaderViews?.size?:0
    }

    fun getHeaderView(): View? {
        return if (getHeaderViewsCount() > 0) mHeaderViews?.get(0) else null
    }

    fun getHeaderViews(): ArrayList<View> {
        return mHeaderViews!!
    }

    override fun destroy() {
        mHeaderViews?.clear()
        mHeaderViews = null
    }

    private fun isHeaderType(itemViewType: Int): Boolean {
        return mHeaderViews?.size?:0 > 0 && mHeaderTypes.contains(itemViewType)
    }

    private fun getHeaderViewByType(itemType: Int): View? {
        return if (!isHeaderType(itemType)) {
            null
        } else mHeaderViews?.get(itemType - HEADER_INIT_INDEX)
    }

    fun getAdapterPosition(isCallback: Boolean, position: Int): Int {
        if (isCallback) {
            val adjPosition = position - getHeaderViewsCount()
            val adapterCount = mInnerAdapter.itemCount
            if (adjPosition < adapterCount) {
                return adjPosition
            }
        } else {
            return position + getHeaderViewsCount()
        }
        return -1
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        mSpanSizeLookup = spanSizeLookup
    }

    interface SpanSizeLookup {
        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
    }

    companion object{

        private const val TYPE_NORMAL = 0
        private const val HEADER_INIT_INDEX = 10002
        private const val TYPE_FOOTER_VIEW = 10001

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

        class FootHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            internal var tips: TextView

            init {
                tips = itemView.findViewById(R.id.tips)
            }
        }
    }
}