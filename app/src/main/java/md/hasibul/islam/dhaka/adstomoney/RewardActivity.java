package md.hasibul.islam.dhaka.adstomoney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RewardActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    ModelClass values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        initializeAll();


        userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelClass values=dataSnapshot.child(userId).getValue(ModelClass.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RewardActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
        ModelClass modelClass=new ModelClass(values.getEmail(),values.getPhone(),values.getUserId(),values.getRewardDate(),String.valueOf(Integer.parseInt(values.getPoints())+100));
        if (values.getRewardDate().equalsIgnoreCase(Utils.todayDate())){
            Toast.makeText(this, "Try after some times later", Toast.LENGTH_SHORT).show();
        }else {
            databaseReference.child(userId).setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RewardActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RewardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void initializeAll(){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
