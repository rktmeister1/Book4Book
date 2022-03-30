package com.example.firsttimeandroidstudio.Model;

public class ListOfChats {
    public String chat_id;

    public ListOfChats(String chat_id) {
        this.chat_id = chat_id;
    }

    public ListOfChats(){}

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
}
