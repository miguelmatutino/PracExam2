package matutino.com.pracexam2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference root;

    EditText eFN, eLN, eAvr ,eX1,eX2;
    TextView Avr;
    int index=0;
    ArrayList<String> keyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        root =   db.getReference("table");
        eFN = findViewById(R.id.fName);
        eLN = findViewById(R.id.lName);
        eX1 = findViewById(R.id.exam1);
        eX2 = findViewById(R.id.exam2);
        Avr = findViewById(R.id.textAvr);
        keyList = new ArrayList<String>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ss: dataSnapshot.getChildren()){
                    keyList.add(ss.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public  void displayAverage(View V){
        String fname = eFN.getText().toString().trim();
        String lname = eLN.getText().toString().trim();
        Long exam1 = Long.parseLong(eX1.getText().toString().trim());
        Long exam2 =Long.parseLong(eX2.getText().toString().trim());
        Long avr = (exam1+exam2)/2;

        Student sgr = new Student(fname,lname,avr);
        String key = root.push().getKey();
        root.child(key).setValue(sgr);
        keyList.add(key);



        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student stud = dataSnapshot.child(keyList.get(index)).getValue(Student.class);
                eFN.setText(stud.getFname());
                eLN.setText(stud.getLname());
                Avr.setText(stud.getAverage().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
      //  displayAve();
        index++;
    }


    /**

     public void displayAve(){
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student stud = dataSnapshot.child(keyList.get(index)).getValue(Student.class);
              eFN.setText(stud.getFname());
              eLN.setText(stud.getLname());
                Avr.setText(stud.getAverage().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

     */
}
