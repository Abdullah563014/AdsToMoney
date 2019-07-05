package md.hasibul.islam.dhaka.adstomoney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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


    LinearLayout rootLayout,progressRootLayout;
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
                values=dataSnapshot.child(userId).getValue(ModelClass.class);
                ModelClass modelClass=new ModelClass(values.getEmail(),values.getPhone(),values.getUserId(),Utils.todayDate(),String.valueOf(Integer.parseInt(values.getPoints())+100));
                if (values.getRewardDate().equalsIgnoreCase(Utils.todayDate())){
                    if (Utils.isUpdated){
                        progressRootLayout.setVisibility(View.GONE);
                        rootLayout.setVisibility(View.VISIBLE);
                    }else {
                        progressRootLayout.setVisibility(View.GONE);
                        rootLayout.setVisibility(View.GONE);
                    }
                    Toast.makeText(RewardActivity.this, "Try again tomorrow", Toast.LENGTH_SHORT).show();
                }else {
                    rootLayout.setVisibility(View.VISIBLE);
                    progressRootLayout.setVisibility(View.GONE);
                    if (!Utils.isUpdated){
                        databaseReference.child(userId).setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Utils.isUpdated=true;
//                                Toast.makeText(RewardActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RewardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressRootLayout.setVisibility(View.GONE);
                rootLayout.setVisibility(View.GONE);
                Toast.makeText(RewardActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initializeAll(){
        rootLayout=findViewById(R.id.rewardActivityRootId);
        progressRootLayout=findViewById(R.id.rewardActivityProgressBarRootId);
        rootLayout.setVisibility(View.GONE);
        progressRootLayout.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
