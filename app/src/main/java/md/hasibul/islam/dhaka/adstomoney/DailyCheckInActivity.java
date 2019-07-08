package md.hasibul.islam.dhaka.adstomoney;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class DailyCheckInActivity extends AppCompatActivity {


    ProgressBar progressBar;
    TextView textView;
    private InterstitialAd interstitialAd;
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
        setContentView(R.layout.activity_daily_check_in);

        progressBar=findViewById(R.id.adsActivityProgressBarId);
        textView=findViewById(R.id.adsActivityTextViewId);

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        showAds();

        loadFirebaseData();
    }



    private void showAds(){
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                hideProgressBar();
                interstitialAd.show();
                Toast.makeText(DailyCheckInActivity.this, "Ads loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                hideProgressBar();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                SharedPreferences sharedPreferences=getSharedPreferences("Counter",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                int value=sharedPreferences.getInt("count",0);
                editor.putInt("count",value+1);
                editor.apply();
                hideProgressBar();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                hideProgressBar();
                Toast.makeText(DailyCheckInActivity.this, "Failed to load ads", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    private void loadFirebaseData(){
        if (databaseReference!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    ModelClass modelClass=dataSnapshot.child(userId).getValue(ModelClass.class);
                    userEmail= Objects.requireNonNull(modelClass).getEmail();
                    userPhone=modelClass.getPhone();
                    userId=modelClass.getUserId();
                    userRewardDate=modelClass.getRewardDate();
                    userPoints=modelClass.getPoints();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DailyCheckInActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            initializeAll();
            loadFirebaseData();
        }
    }

    private void initializeAll(){
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please wait, Your data uploading to database", Toast.LENGTH_SHORT).show();
        if (userId!=null || !userId.isEmpty()){
            ModelClass modelClass=new ModelClass(userEmail,userPhone,userId,Utils.todayDate(),String.valueOf(Integer.parseInt(userPoints)+50));
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(modelClass).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                finish();
                            }
                        }
                    }
            );
        }else {
            Toast.makeText(this, "Please wait some time and try again", Toast.LENGTH_SHORT).show();
        }
    }


}
