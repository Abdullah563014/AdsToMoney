package md.hasibul.islam.dhaka.adstomoney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, phoneEditText;
    Button registrationButton;
    ProgressBar progressBar;
    String email, password, phone;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        initializeAll();

        progressBar.setVisibility(View.GONE);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationMethod();
            }
        });
    }


    private void initializeAll() {
        emailEditText = findViewById(R.id.registrationEmailEditTextId);
        passwordEditText = findViewById(R.id.registrationPasswordEditTextId);
        phoneEditText = findViewById(R.id.registrationPhoneNumberEditTextId);
        registrationButton = findViewById(R.id.registrationButtonId);
        progressBar = findViewById(R.id.registrationProgressBarId);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    private void registrationMethod() {
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        phone = phoneEditText.getText().toString();

        if (email == null || email.isEmpty()) {
            emailEditText.setError("Please input your email address");
            emailEditText.setFocusable(true);
        }
        if (password == null || password.isEmpty()) {
            passwordEditText.setError("Please input your password");
            passwordEditText.setFocusable(true);
        }
        if (password!=null && !password.isEmpty() && password.length()<=5) {
            passwordEditText.setError("Please input at lest 6 digits password");
            passwordEditText.setFocusable(true);
        }
        if (phone == null || phone.isEmpty()) {
            phoneEditText.setError("Please input your email address");
            phoneEditText.setFocusable(true);
        }
        if (email != null && !email.isEmpty() && !isValidateEmail(email)) {
            emailEditText.setError("Your email address is invalid");
            emailEditText.setFocusable(true);
        }


        if (email != null && !email.isEmpty() && password != null && !password.isEmpty() && password.length()>5 && phone != null && !phone.isEmpty() && isValidateEmail(email)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                ModelClass modelClass = new ModelClass(email, phone, userId,Utils.todayDate(),"0");
                                databaseReference.child(userId).setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserRegistrationActivity.this, "Account Created Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(UserRegistrationActivity.this, "User data not saved successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(UserRegistrationActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserRegistrationActivity.this, "Failed to create account "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private boolean isValidateEmail(String userEmail) {
        if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            return true;
        } else {
            return false;
        }
    }


}
