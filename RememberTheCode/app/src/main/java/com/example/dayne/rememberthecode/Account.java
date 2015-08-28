package com.example.dayne.rememberthecode;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;


@Table(name = "Accounts")
public class Account extends Model {

    @Column(name = "account")
    private String task;

    public Account() {
        super();
    }

    public void saveTask(){
        save();
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void deleteTask(){
        long i = getId();
        new Delete().from(Account.class).where("Id = ?", i).execute();
    }}


