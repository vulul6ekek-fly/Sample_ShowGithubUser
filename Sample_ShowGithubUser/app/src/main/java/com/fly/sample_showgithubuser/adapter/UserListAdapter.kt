package com.fly.sample_showgithubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fly.sample_showgithubuser.R
import com.fly.sample_showgithubuser.data.vo.UserListVO
import com.fly.sample_showgithubuser.view.RoundImageView

class UserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var mList: MutableList<UserListVO>
    private var adapterAction: UserListAdapterAction

    constructor(adapterAction: UserListAdapterAction) : super(){
        mList = mutableListOf()
        this.adapterAction = adapterAction
    }

    fun addData(data: List<UserListVO>) {
        this.mList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_userlist, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(mList.get(position)) {
            when (holder) {
                is UserViewHolder -> {
                    holder.tv_name.text = this.login?:""
                    if(this.site_admin?:false){
                        holder.tv_admin.visibility = View.VISIBLE
                    }else{
                        holder.tv_admin.visibility = View.GONE
                    }
                    Glide.with(holder.ri_user.context)
                        .load(this.avatar_url?:"")
                        .into(holder.ri_user)
                    if(position == (getItemCount()-1)){
                        holder.rl_line.visibility = View.VISIBLE
                    }else{
                        holder.rl_line.visibility = View.GONE
                    }
                    holder.itemView.setOnClickListener { adapterAction.onItemClick(this) }
                }
                else -> { }
            }
        }
    }

    fun getData(position: Int): UserListVO = mList[position]

    override fun getItemCount(): Int = mList.size

    companion object {

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            internal var ri_user: RoundImageView
            internal var tv_name: TextView
            internal var tv_admin: TextView
            internal var rl_line: RelativeLayout

            init {
                ri_user = itemView.findViewById(R.id.ri_user)
                tv_name = itemView.findViewById(R.id.tv_name)
                tv_admin = itemView.findViewById(R.id.tv_admin)
                rl_line = itemView.findViewById(R.id.rl_line)
            }
        }
    }

    interface UserListAdapterAction {
        fun onItemClick(user: UserListVO)
    }
}