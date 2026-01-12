package com.example.booktalk.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.booktalk.R;
import com.example.booktalk.controller.AuthController;
import com.example.booktalk.ui.main.MainActivity;

/**
 * Register Activity (MVC)
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private AuthController authController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        authController = new AuthController(this);
        
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            authController.register(name, username, password, null, new AuthController.Callback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                }
                
                @Override
                public void onError(String message) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                
                @Override
                public void onNavigateToMain() {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        });
        
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}

