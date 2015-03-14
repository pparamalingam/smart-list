package com.smartlist.preshoth.smartlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseQuery.CachePolicy;

public class TodoActivity extends Activity implements OnItemClickListener {

    private EditText _taskInput;
    private ListView _listView;
    private TaskAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Parse.initialize(this, "8xQUnEJtM1YmSi1RTFoSreIBOUSauy6QY2pQiGd4", "BIqocgosgH4jKrR2D1Uwr3jDUs4DIHRWHkExNzJ1");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Task.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        _adapter = new TaskAdapter(this, new ArrayList<Task>());

        _taskInput = (EditText) findViewById(R.id.task_input);
        _listView = (ListView) findViewById(R.id.task_list);
        _listView.setAdapter(_adapter);
        _listView.setOnItemClickListener(this);

        updateData();
    }

    public void updateData(){
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException error) {
                if(tasks != null){
                    _adapter.clear();
                    for (int i = 0; i < tasks.size(); i++) {
                        _adapter.add(tasks.get(i));
                    }
                }
            }
        });
    }
    public void createTask(View v) {
        if (_taskInput.getText().length() > 0){
            Task t = new Task();
            t.setACL(new ParseACL(ParseUser.getCurrentUser()));
            t.setUser(ParseUser.getCurrentUser());
            t.setDescription(_taskInput.getText().toString());
            t.setCompleted(false);
            t.saveEventually();
            _adapter.insert(t, 0);
            _taskInput.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task task = _adapter.getItem(position);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

        task.setCompleted(!task.isCompleted());

        if(task.isCompleted()){
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        task.saveEventually();
    }

}
