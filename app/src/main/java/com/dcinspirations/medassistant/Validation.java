package com.dcinspirations.medassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dcinspirations.medassistant.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;


public class Validation extends AppCompatActivity {
    EditText name,email,pass;
    RelativeLayout bt;
    String selection;
    TextView at;
    GifImageView giv;
    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        tb = findViewById(R.id.toolbar);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Login"),true);
        tabs.addTab(tabs.newTab().setText("Register"));
        selection = "login";

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        at = findViewById(R.id.at);
        giv = findViewById(R.id.lgif);
        bt = findViewById(R.id.action);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selection.equalsIgnoreCase("login")) {

                    SignIn();
                }else{

                    SignUp();
                }
            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().equalsIgnoreCase("Login")){
                    name.setVisibility(View.GONE);
                    at.setText("Login");
                    selection = "Login";
                    giv.setVisibility(View.INVISIBLE);
                }else{
                    name.setVisibility(View.VISIBLE);
                    at.setText("Register");
                    selection = "Register";
                    giv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void SignIn(){
        String emtext = email.getText().toString().trim();
        String ptext = pass.getText().toString().trim();
        if(emtext.isEmpty()){
            email.requestFocus();
            email.setError("Enter Email");
            return;
        }
        if(ptext.isEmpty()){
            pass.requestFocus();
            pass.setError("Enter Password");
            return;
        }

        at.setText("Logging In...");
        giv.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emtext,ptext)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserModel ud = dataSnapshot.getValue(UserModel.class);
                                    try {
                                        new Sp().setLoggedIn(true, ud.Name, ud.Email,ud.Disorder,Integer.parseInt(ud.Gender),FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        startActivity(new Intent(Validation.this,Dash.class));
                                        finish();
                                    }catch (Exception e){
                                        Toast.makeText(Validation.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        at.setText("Login");
                                        giv.setVisibility(View.INVISIBLE);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }else{
                            Toast.makeText(Validation.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            at.setText("Login");
                            giv.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void SignUp(){
        final String emtext = email.getText().toString().trim();
        String ptext = pass.getText().toString().trim();
        final String ntext = name.getText().toString().trim();
        if(ntext.isEmpty()){
            name.requestFocus();
            name.setError("Enter Name");
            return;
        }
        if(emtext.isEmpty()){
            email.requestFocus();
            email.setError("Enter Email");
            return;
        }
        if(ptext.isEmpty()){
            pass.requestFocus();
            pass.setError("Enter Password");
            return;
        }
        at.setText("Creating User...");
        giv.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emtext,ptext)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Name").setValue(ntext);
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Email").setValue(emtext);
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Disorder").setValue("");
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Gender").setValue("0");
                            new Sp().setLoggedIn(true,ntext,emtext,"",0,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            startActivity(new Intent(Validation.this,Dash.class));
                            finish();
                        }else{
                            Toast.makeText(Validation.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            at.setText("Register");
                            giv.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}