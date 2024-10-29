package com.learning.recyclerviewandboundservice

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.recyclerviewandboundservice.dto.UserDto


class CustomAdapter(
    val context: Context,
    val userList: MutableList<UserDto>) :
    RecyclerView.Adapter<CustomAdapter.CommentHolder>(){


    /*이벤트 처리를 위한 Listener*/
    //클릭 인터페이스 정의. Activity에서 이 interface 구현체를 만들어서 호출한다.
    interface OnBoardClickListener {
        fun onEditPage(id: Int)
    }

    //클릭리스너 선언
    private lateinit var boardClickListener: OnBoardClickListener

    //클릭리스너 등록 매소드
    fun setOnBoardClickListener(boardClickListener: OnBoardClickListener) {
        this.boardClickListener = boardClickListener
    }


    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        private val itemView = itemView.findViewById<LinearLayout>(R.id.itemView)

        private val name: TextView = itemView.findViewById(R.id.itemName)
        private val age: TextView = itemView.findViewById(R.id.itemAge)
        private val gender: TextView = itemView.findViewById(R.id.itemGender)

        fun bind(user: UserDto) {
            name.text = user.name
            age.text = user.age.toString()
            gender.text = user.gender

            itemView.setOnClickListener {
                boardClickListener.onEditPage(userList[layoutPosition].id)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.CommentHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CustomAdapter.CommentHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }





}

