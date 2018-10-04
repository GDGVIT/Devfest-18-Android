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
        var questions: ArrayList<Question>,
        var userId: String
) : RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuestionViewHolder(parent.inflate(R.layout.item_question_details))

    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) = holder.bind(questions[position], userId)

    fun updateList(newQuestions: ArrayList<Question>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

}

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(question: Question, userId: String) = with(itemView) {

        text_question_upvotes.text = question.upvotes.toInt().toString()
        text_question.text = question.question

        if (userId in question.upVotedList) {
            ImageViewCompat.setImageTintList(image_agenda_light_icon, ColorStateList.valueOf(resources.getColor(R.color.colorPrimary)))
        }
    }
}