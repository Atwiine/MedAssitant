package com.dcinspirations.medassistant;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetails extends AppCompatActivity {

    String[] Datalist = {"Male", "Female"};
    ArrayAdapter<String> adapter;
    Spinner mt;
    TextView editnsave;
    EditText name,email,disorder;
    String[] basics = new Sp().getBasics();
    Toolbar tb;
    ImageView pimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        tb = findViewById(R.id.toolbar);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pimg = findViewById(R.id.pimg);
        pimg.setImageResource(Integer.parseInt(basics[3])==0?R.drawable.contact2:R.drawable.female);
        name=findViewById(R.id.name);
        email = findViewById(R.id.email);
        disorder = findViewById(R.id.disorder);

        name.setText(basics[0]);
        email.setText(basics[1]);
        disorder.setText(basics[2]);

        mt = findViewById(R.id.gender);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Datalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mt.setAdapter(adapter);
        mt.setSelection(Integer.parseInt(basics[3]));

        editnsave = findViewById(R.id.save);
        editnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editnsave.getText().toString();
                if (s.equalsIgnoreCase("edit")) {
                    setEdit();
                } else {
                    saveDetails();
                }
            }
        });
    }

    private void setEdit() {
        editnsave.setText("Save");
        name.setEnabled(true);
        disorder.setEnabled(true);
        mt.setEnabled(true);
    }

    private void saveDetails() {
        final String n = name.getText().toString();
        final String d = disorder.getText().toString();
        final String m = Integer.toString(mt.getSelectedItemPosition());
        pimg.setImageResource(m.equalsIgnoreCase("male")?R.drawable.contact2:R.drawable.female);
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        dbref.child(basics[4]).child("Name").setValue(n);
        dbref.child(basics[4]).child("Disorder").setValue(d);
        dbref.child(basics[4]).child("Gender").setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new Sp().setLoggedIn(true,n,basics[1],d,m.equalsIgnoreCase("male")?0:1,basics[4]);
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Save Successful", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }
}
