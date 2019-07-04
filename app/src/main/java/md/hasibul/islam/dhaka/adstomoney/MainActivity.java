package md.hasibul.islam.dhaka.adstomoney;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button logInButton, createAccountButton;
    ProgressBar logInProgressBar;
    private String userEmail, userPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAll();

        logInProgressBar.setVisibility(View.GONE);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInButtonMethod();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountMethod();
            }
        });


    }


    private void initializeAll() {
        emailEditText = findViewById(R.id.logInEmailEditTextId);
        passwordEditText = findViewById(R.id.logInPasswordEditTextId);
        logInButton = findViewById(R.id.logInButtonId);
        createAccountButton = findViewById(R.id.createAccountButtonId);
        logInProgressBar = findViewById(R.id.logInProgressBarId);

        mAuth = FirebaseAuth.getInstance();
    }

    private void logInButtonMethod() {
        userEmail = emailEditText.getText().toString().trim();
        userPassword = passwordEditText.getText().toString();


        if (userEmail == null || userEmail.isEmpty()) {
            emailEditText.setError("Please input your email address");
            emailEditText.setFocusable(true);
        }
        if (userPassword == null || userPassword.isEmpty()) {
            passwordEditText.setError("Please input your email address");
            passwordEditText.setFocusable(true);
        }
        if (userEmail != null && !userEmail.isEmpty() && !isValidateEmail(userEmail)) {
            emailEditText.setError("Your email address is invalid");
            emailEditText.setFocusable(true);
        }


        if (userPassword != null && !userPassword.isEmpty() && userEmail != null && !userEmail.isEmpty() && isValidateEmail(userEmail)) {
            logInProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                logInProgressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                logInProgressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void createAccountMethod() {
        Intent intent=new Intent(this,UserRegistrationActivity.class);
        startActivity(intent);
        finish();
    }



    private boolean isValidateEmail(String userEmail) {
        if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
