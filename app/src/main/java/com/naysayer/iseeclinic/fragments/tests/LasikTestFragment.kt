package com.naysayer.iseeclinic.fragments.tests


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.naysayer.iseeclinic.R
import kotlinx.android.synthetic.main.fragment_lasik_test.*


class LasikTestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lasik_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardStackView.setAdapter(createQuestionCardAdapter())
    }

    private fun createQuestionCardAdapter(): QuestionCardAdapter {
        val adapter = QuestionCardAdapter(context)
        adapter.addAll(createQuestions())
        return adapter
    }

    private fun createQuestions(): List<Question> {
        val questions = ArrayList<Question>()
        questions.add(Question(question = getString(R.string._1_question)))
        questions.add(Question(question = getString(R.string._2_question)))
        questions.add(Question(question = getString(R.string._3_question)))
        questions.add(Question(question = getString(R.string._4_question)))
        questions.add(Question(question = getString(R.string._5_question)))
        questions.add(Question(question = getString(R.string._6_question)))
        questions.add(Question(question = getString(R.string._7_question)))
        questions.add(Question(question = getString(R.string._8_question)))
        return questions
    }

}

