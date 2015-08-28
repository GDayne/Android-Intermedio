package com.example.dayne.rememberthecode;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends BaseAdapter {
    private Context context;
    private List<Account> list;

    public TasksAdapter(Context context, List<Account> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int res = -1;
        if (list != null)
            res = list.size();
        return res;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (list != null)
            item = list.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.task, parent, false);
        }
        final Account account = (Account) getItem(position);

        TextView txtAccount = (TextView) convertView.findViewById(R.id.txtTask);


        Button button = (Button) convertView.findViewById(R.id.btnDone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.deleteTask();
                ((MainActivity)context).updateUI();
            }
        });
        txtAccount.setText(account.getTask());


        return convertView;
    }
}