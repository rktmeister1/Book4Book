package com.example.firsttimeandroidstudio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firsttimeandroidstudio.Adapter.UserAdapter;
import com.example.firsttimeandroidstudio.Model.Chat;
import com.example.firsttimeandroidstudio.Model.ListOfChats;
import com.example.firsttimeandroidstudio.Model.User;
import com.example.firsttimeandroidstudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> users;

    FirebaseUser fUser;
    DatabaseReference dRef;

    private List<String> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();

        dRef = FirebaseDatabase.getInstance().getReference("Chats");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    Chat chat = s.getValue(Chat.class);
                    if (chat.getSender().equals(fUser.getUid())){
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fUser.getUid())){
                        userList.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void readChats(){
        users = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference("Users");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot s: snapshot.getChildren()){
                    User user = s.getValue(User.class);
                    for (String id: userList){
                        if (user.getId().equals(id)){
                            if (users.size() != 0){
                                for (User user1: users){
                                    if (!user.getId().equals(user1.getId())){
                                        users.add(user);
                                    }
                                }
                            } else {
                                users.add(user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), users);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void chatList(){
//        users = new ArrayList<>();
//        dRef = FirebaseDatabase.getInstance().getReference("Users");
//        dRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for (DataSnapshot s: snapshot.getChildren()){
//                    User user = s.getValue(User.class);
//                    for (ListOfChats c: userList){
//                        if (user.getId().equals(c.getChat_id())){
//                            users.add(user);
//                        }
//                    }
//                }
//                userAdapter = new UserAdapter(getContext(), users);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
}
