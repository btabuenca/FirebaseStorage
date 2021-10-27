package es.upm.miw.firebasestorage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            setContentView(R.layout.activity_login);

            final EditText editTextEmail = findViewById(R.id.editTextEmail);
            final EditText editTextPassword = findViewById(R.id.editTextPassword);
            final Button buttonSignUp = findViewById(R.id.buttonSignUp);
            final Button buttonSignIn = findViewById(R.id.buttonSignIn);

            buttonSignUp.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    final String email = editTextEmail.getText().toString();
                    final String password = editTextPassword.getText().toString();

                    signUp(email, password);
                }
            });

            buttonSignIn.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    final String email = editTextEmail.getText().toString();
                    final String password = editTextPassword.getText().toString();

                    signIn(email, password);
                }
            });
        } else {
            showHome(currentUser);
        }


    }

    private void signUp(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign up failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign in failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showHome(@NonNull FirebaseUser user)
    {
        Intent intent = new Intent(this, MainActivity.class);
        if (user != null){
            Log.d(TAG, "Login for user: "+user.getEmail());
            intent.putExtra("email", user.getEmail());

        }else{
            Log.d(TAG, "Login with anonymous user.");
            intent.putExtra("email", "");
        }


        startActivity(intent);
        finish(); // Do not go back to login when back triggered.
    }
}