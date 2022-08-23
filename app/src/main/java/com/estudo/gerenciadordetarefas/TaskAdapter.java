package com.estudo.gerenciadordetarefas;
//06


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

        private final LayoutInflater mLayoutInflater;
        private View.OnClickListener mOnItemClickListener;
        private View.OnLongClickListener mOnLongClickListener;
        private List<Task>mTasksList;
        private List<Task>mTaskListFull;


        public TaskAdapter(Context context){
            mLayoutInflater = LayoutInflater.from(context);
        }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroupParent, int viewType){
            View itemView = mLayoutInflater.inflate(R.layout.card_item_task_list,viewGroupParent,false);
            return new TaskViewHolder(itemView);

        }

        public String convertFromLongDateToDateFormat(long longDate){
            return new SimpleDateFormat("dd/MM/yyyy").format(new Date(longDate));

        }

        public String setShowColor(int isFinished){

            if ( isFinished == 1 ){

                return "#299e4a";

            }else{

                return "#000000";
            }
        }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int position){

            Task currentTask = mTasksList.get(position);
            taskViewHolder.mTextViewDescription.setText(currentTask.getDescription());
            taskViewHolder.mTextViewEstimatedDate.setText(convertFromLongDateToDateFormat(currentTask.getEstimatedDate()));
           // taskViewHolder.mRatingBarPriorityLevel.setRating(currentTask.getPriorityLevel());
            taskViewHolder.mTextViewDescription.setTextColor(Color.parseColor(String.valueOf(setShowColor(currentTask.getIsFinished()))));
            taskViewHolder.mTextViewEstimatedDate.setTextColor(Color.parseColor(String.valueOf(setShowColor(currentTask.getIsFinished()))));

        }

    @Override
    public int getItemCount(){
            if ( mTasksList != null ){
                return mTasksList.size();
            }else{
                return 0;
            }
    }

    public void setTaskList(List<Task>tasks){
            mTasksList = tasks;
            mTaskListFull = new ArrayList<>(tasks);
            notifyDataSetChanged();
    }

    public Task getTaskAt(int position){
            return mTasksList.get(position);
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
            mOnItemClickListener = itemClickListener;

    }
    public void setOnLongClickListener(View.OnLongClickListener itemLongClickListener){
            mOnLongClickListener = itemLongClickListener;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
            private final TextView mTextViewDescription;
            private final TextView mTextViewEstimatedDate;
           // private final RatingBar mRatingBarPriorityLevel;
            private TaskViewHolder(View itemView){
                super(itemView);
                mTextViewDescription = itemView.findViewById(R.id.textView_description);
                mTextViewEstimatedDate = itemView.findViewById(R.id.textView_estimated_date);
              //  mRatingBarPriorityLevel = itemView.findViewById(R.id.rating_task_priority_level);
                itemView.setTag(this);
                itemView.setOnClickListener(mOnItemClickListener);
                itemView.setOnLongClickListener(mOnLongClickListener);


            }
    }

    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Task> filteredList = new ArrayList<>();
            if ( charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mTaskListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Task task : mTaskListFull){
                    if ( task.getDescription().toLowerCase().contains(filterPattern) ){
                        filteredList.add(task);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mTasksList.clear();
            mTasksList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    @Override
    public Filter getFilter(){return taskFilter;}

}
