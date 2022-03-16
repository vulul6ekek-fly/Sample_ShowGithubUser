package com.fly.sample_showgithubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fly.sample_showgithubuser.R
import com.fly.sample_showgithubuser.adapter.UserListAdapter
import com.fly.sample_showgithubuser.contract.UserListContract
import com.fly.sample_showgithubuser.data.dao.UserListDAO
import com.fly.sample_showgithubuser.data.vo.UserListVO
import com.fly.sample_showgithubuser.extension.KEY_LIGIN
import com.fly.sample_showgithubuser.footer_lrecyclerView.LRecyclerFooterModel
import com.fly.sample_showgithubuser.presenter.UserListPresenter
import com.fly.sample_showgithubuser.repository.UserListRepository

class MainActivity : AppCompatActivity(),LRecyclerFooterModel.LRecyclerFooterAction,
    UserListAdapter.UserListAdapterAction, UserListContract.View {

    private lateinit var lRecyclerFooterModel: LRecyclerFooterModel
    private lateinit var presenter: UserListPresenter
    private lateinit var adapter: UserListAdapter
    private val repository by lazy { UserListRepository() }
    private var isHasMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModel()
        initView()
        getData()
    }

    override fun getPage(since: Int, pageNumber: Int) {
        repository.getUsers(UserListDAO(since,pageNumber),{userList ->
            closeFooter()
            userList?.let { adapter.addData(it) }
        },::apiFail)
    }

    override fun finishGetValue() {
        isHasMore = false
        updateList(isHasMore)
    }

    override fun onItemClick(user: UserListVO) {
        startActivity(Intent(this, UserDetailActivity::class.java).apply { putExtra(KEY_LIGIN, user.login) })
    }

    override fun getMoreData() {
        if (isHasMore) {
            getData()
        } else {
            updateList(isHasMore)
        }
    }

    override fun initInnerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        adapter = UserListAdapter(this)
        return adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        lRecyclerFooterModel.releaseResources()
    }

    private fun apiFail(errMessage: String) {
        updateList(isHasMore)
        Toast.makeText(this, errMessage, Toast.LENGTH_LONG).show()
    }

    private fun updateList(hasMore: Boolean) {
        lRecyclerFooterModel.updateList(hasMore)
    }

    private fun getData(){
        presenter.checkPage(if(adapter.itemCount == 0) null else adapter.getData(adapter.itemCount-1),adapter.itemCount)
    }

    private fun closeFooter() {
        lRecyclerFooterModel.closeLoading()
    }

    private fun initModel() {
        lRecyclerFooterModel = LRecyclerFooterModel(this, this)
        presenter = UserListPresenter(this)
    }

    private fun initView(){
        lRecyclerFooterModel.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header_userlist, RelativeLayout(this)))
    }
}