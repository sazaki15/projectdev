package android.security.checking;

import android.content.Intent;


import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText userNameField = findViewById(R.id.loginUserName);
        EditText passwordField = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginBtn);
        Button registerButton = findViewById(R.id.regBtn);
        Button viewUsersButton = findViewById(R.id.viewUsersBtn);

        loginButton.setOnClickListener(v -> {
            String username = userNameField.getText().toString();
            String password = passwordField.getText().toString();

            User user = UserManager.findUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, registration.class);
            startActivity(intent);
        });

        viewUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        });

        Button biometricButton = findViewById(R.id.biometricButton);

        // Initialize the BiometricManager
        BiometricManager biometricManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            biometricManager = (BiometricManager) getSystemService(BIOMETRIC_SERVICE);
        }

        if (biometricManager == null) {
            Toast.makeText(this, "BiometricManager not available on this device", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    // Biometric is supported and ready to use
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_SHORT).show();
                    return;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Toast.makeText(this, "Biometric hardware is currently unavailable", Toast.LENGTH_SHORT).show();
                    return;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Toast.makeText(this, "No biometric credentials enrolled", Toast.LENGTH_SHORT).show();
                    return;
            }
        }

        // Set up the BiometricPrompt
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt(login.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(login.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(login.this, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                    // Proceed to the next screen or perform login logic here
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            biometricPrompt = null;
        }

        // BiometricPrompt Info
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use your fingerprint to log in")
                .setNegativeButtonText("Cancel")
                .build();

        // Button Click Listener
        biometricButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }
}
