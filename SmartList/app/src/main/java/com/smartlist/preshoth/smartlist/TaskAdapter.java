package com.smartlist.preshoth.smartlist;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context _Context;
    private List<Task> _Tasks;

    public TaskAdapter(Context context, List<Task> objects) {
        super(context, R.layout.task_row_item, objects);
        this._Context = context;
        this._Tasks = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater _layoutinfralter = LayoutInflater.from(_Context);
            convertView = _layoutinfralter.inflate(R.layout.task_row_item, null);
        }

        Task task = _Tasks.get(position);
        TextView description = (TextView) convertView.findViewById(R.id.task_description);
        description.setText(task.getDescription());

        if(task.isCompleted()){
            description.setPaintFlags(description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            description.setPaintFlags(description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        return convertView;
    }

}
