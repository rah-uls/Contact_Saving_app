package com.example.firebaseauth;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity {
    private TextView name, email;
    private Button logout;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private RecyclerAdapterr adapter;
    private ArrayList<DataModel> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private NavigationView navigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawablelayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //find all resourses
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        logout = findViewById(R.id.userlogout);
        logout.setText(R.string.logout);
        recyclerView=findViewById(R.id.recyclerView);
        floatingActionButton=findViewById(R.id.floatingaction);
        drawerLayout=findViewById(R.id.drawablelayout);
        navigationView = findViewById(R.id.navigationview);
        toolbar = findViewById(R.id.toolbaaar);

        //set toolbar
        setSupportActionBar(toolbar);

        //drawerLayout setup in toolbar
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        //user email and name get
        if (auth.getCurrentUser() != null) {
            name.setText(auth.getCurrentUser().getDisplayName());
            email.setText(auth.getCurrentUser().getEmail());
        }
        //logout from application
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, loginactivity.class));
                finish();
            }
        });
        toggle.syncState();

        //recyclerView layout Setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get data from real time firebaseDatabase and set up in RecyclerView
        FirebaseRecyclerOptions<DataModel> options=new FirebaseRecyclerOptions.Builder<DataModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("task"),DataModel.class).build();
        adapter=new RecyclerAdapterr(options);
        recyclerView.setAdapter(adapter);

            //add button setUp in DialogBox
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog=new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.add_item);
                    Button button;
                    TextInputEditText nameee,mobileee;
                    nameee=dialog.findViewById(R.id.name);
                    mobileee=dialog.findViewById(R.id.mobile);
                    button=dialog.findViewById(R.id.add);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String namee=nameee.getText().toString();
                            String mobilee=mobileee.getText().toString();
//                            DataModel dataModel=new DataModel("name","mobilw",id);
                            if (namee.equals("")){
                                nameee.setError("please enter your name");
                                nameee.requestFocus();
                                return;
                            } else if (mobilee.equals("")) {
                                mobileee.setError("please enter age");
                                mobileee.requestFocus();
                                return;
                            }
                            else {
                                //add Data in real time firebaseDatabase
                                HashMap<String,Object> hashMap= new HashMap();
                                hashMap.put("name",namee.toString());
                                hashMap.put("mobile", mobilee.toString());
                                FirebaseDatabase.getInstance().getReference().child("task").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,"error"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
        }
        });

    }
    //check current user are exist or not
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        if (auth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, registrationuser.class));
            finish();
        }
    }

    //back button manage
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    //activity lifecycle when activity in stop mode
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}