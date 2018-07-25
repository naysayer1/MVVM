package com.naysayer.iseeclinic.fragments.tests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.naysayer.iseeclinic.R;

import java.util.Objects;

public class QuestionCardAdapter extends ArrayAdapter<Question> {
    private Context context;

    public QuestionCardAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View contentView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.question_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        Question question = getItem(position);

        holder.question.setText(Objects.requireNonNull(question).getQuestion());
        holder.question.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        switch (position) {
            case 0:
                holder.radioButton1.setText(context.getString(R.string.answer_to_the_1_question_option_1));
                holder.radioButton2.setText(context.getString(R.string.answer_to_the_1_question_option_2));
                holder.radioButton3.setText(context.getString(R.string.answer_to_the_1_question_option_3));
                holder.radioButton4.setVisibility(View.GONE);
                break;
            case 1:
                holder.radioButton1.setText(context.getString(R.string.answer_to_the_2_question_option_1));
                holder.radioButton2.setText(context.getString(R.string.answer_to_the_2_question_option_2));
                holder.radioButton3.setText(context.getString(R.string.answer_to_the_2_question_option_3));
                holder.radioButton4.setText(context.getString(R.string.answer_to_the_2_question_option_4));
                break;
            case 2:
                holder.radioButton1.setText(context.getString(R.string.answer_to_the_3_question_option_1));
                holder.radioButton2.setText(context.getString(R.string.answer_to_the_3_question_option_2));
                holder.radioButton3.setText(context.getString(R.string.answer_to_the_3_question_option_3));
                holder.radioButton4.setText(context.getString(R.string.answer_to_the_3_question_option_4));
                break;
            case 3:
                holder.radioButton1.setText(context.getString(R.string.simple_positive_answer));
                holder.radioButton2.setText(context.getString(R.string.simple_negative_answer));
                holder.radioButton3.setVisibility(View.GONE);
                holder.radioButton4.setVisibility(View.GONE);
                break;
            case 4:
                holder.radioButton1.setText(context.getString(R.string.simple_positive_answer));
                holder.radioButton2.setText(context.getString(R.string.simple_negative_answer));
                holder.radioButton3.setVisibility(View.GONE);
                holder.radioButton4.setVisibility(View.GONE);
                break;
            case 5:
                holder.radioButton1.setText(context.getString(R.string.simple_positive_answer));
                holder.radioButton2.setText(context.getString(R.string.simple_negative_answer));
                holder.radioButton3.setVisibility(View.GONE);
                holder.radioButton4.setVisibility(View.GONE);
                break;
            case 6:

                holder.radioButton1.setVisibility(View.GONE);
                holder.radioButton2.setVisibility(View.GONE);
                holder.radioButton3.setVisibility(View.GONE);
                holder.radioButton4.setVisibility(View.GONE);
                holder.editText.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.radioButton1.setVisibility(View.GONE);
                holder.radioButton2.setVisibility(View.GONE);
                holder.radioButton3.setVisibility(View.GONE);
                holder.radioButton4.setVisibility(View.GONE);
                holder.editText.setVisibility(View.VISIBLE);
                break;
        }
        return contentView;
    }

    private static class ViewHolder {
        public TextView question;
        public RadioGroup radioGroup;
        public RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
        public EditText editText;

        ViewHolder(View view) {
            this.question = view.findViewById(R.id.textView_lasik_test_question);
            this.radioGroup = view.findViewById(R.id.radio_group_lasik_test_answers);
            this.radioButton1 = view.findViewById(R.id.radio_button_lasik_test_answers_1);
            this.radioButton2 = view.findViewById(R.id.radio_button_lasik_test_answers_2);
            this.radioButton3 = view.findViewById(R.id.radio_button_lasik_test_answers_3);
            this.radioButton4 = view.findViewById(R.id.radio_button_lasik_test_answers_4);
            this.editText = view.findViewById(R.id.edit_text_lasik_test_answers);
        }
    }
}


