package com.dscvit.android.devfest18.ui.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Question
import com.dscvit.android.devfest18.utils.getColourId
import com.dscvit.android.devfest18.utils.inflate
import kotlinx.android.synthetic.main.item_agenda_light.view.*
import kotlinx.android.synthetic.main.item_question_details.view.*

class QuestionAdapter(
        var questions: ArrayList<Question?>?,
        var userId: String,
        val listener: (Question) -> Unit
) : RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuestionViewHolder(parent.inflate(R.layout.item_question_details))

    override fun getItemCount() = questions?.size ?: 0

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        questions?.get(position)?.let {
            holder.bind(it, userId, listener)
        }
    }

    fun updateList(newQuestions: ArrayList<Question?>?) {
        questions = newQuestions
        notifyDataSetChanged()
    }

}

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(question: Question, userId: String, listener: (Question) -> Unit) = with(itemView) {

        text_question_upvotes.text = question.upvotes.toInt().toString()
        text_question.text = question.question

        if (userId in question.upVotedList) {
            ImageViewCompat.setImageTintList(button_question_upvote, ColorStateList.valueOf(resources.getColor(R.color.colorPrimary)))
        } else {
            ImageViewCompat.setImageTintList(button_question_upvote, ColorStateList.valueOf(resources.getColor(R.color.white_90)))
        }

        button_question_upvote.setOnClickListener { listener(question) }

        chip_question_tag.text = question.tag
    }
}