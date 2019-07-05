package md.hasibul.islam.dhaka.adstomoney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button dailyRewardButton,dailyCheckInButton,contactUsButton,checkBalanceButton;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private String userEmail;
    private String userPhone;
    private String userId;
    private String userRewardDate;
    private String userPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeAll();

        loadFirebaseData();

    }

    private void initializeAll(){
        dailyRewardButton=findViewById(R.id.dailyRewardButtonId);
        dailyCheckInButton=findViewById(R.id.dailyCheckIndButtonId);
        contactUsButton=findViewById(R.id.contactUsButtonId);
        checkBalanceButton=findViewById(R.id.checkBalanceButtonId);

        dailyRewardButton.setOnClickListener(this);
        dailyCheckInButton.setOnClickListener(this);
        contactUsButton.setOnClickListener(this);
        checkBalanceButton.setOnClickListener(this);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dailyRewardButtonId:
                dailyRewardMethod();
                break;

            case R.id.dailyCheckIndButtonId:
                dailyCheckInMethod();
                break;

            case R.id.contactUsButtonId:
                contactUsMethod();
                break;

            case R.id.checkBalanceButtonId:
                checkBalanceMethod();
                break;
        }
    }


    private void dailyRewardMethod(){
        Intent intent=new Intent(this,RewardActivity.class);
        startActivity(intent);
    }

    private void dailyCheckInMethod(){

    }

    private void contactUsMethod(){

    }

    private void checkBalanceMethod(){
        if (userPoints!=null && !userPoints.isEmpty()){
            Toast.makeText(this, "Your Points is "+userPoints, Toast.LENGTH_LONG).show();
        }
    }

    private void loadFirebaseData(){
        if (databaseReference!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userId=firebaseAuth.getCurrentUser().getUid();
                    ModelClass modelClass=dataSnapshot.child(userId).getValue(ModelClass.class);
                    userEmail=modelClass.getEmail();
                    userPhone=modelClass.getPhone();
                    userId=modelClass.getUserId();
                    userRewardDate=modelClass.getRewardDate();
                    userPoints=modelClass.getPoints();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            initializeAll();
            loadFirebaseData();
        }
    }
}
