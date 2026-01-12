package com.example.booktalk.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.booktalk.R;
import com.example.booktalk.controller.SettingsController;
import com.example.booktalk.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

/**
 * Settings Fragment (MVC)
 */
public class SettingsFragment extends Fragment {
    private EditText etName;
    private EditText etUsername;
    private MaterialButton btnSave;
    private MaterialButton btnLogout;
    private SettingsController settingsController;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        settingsController = new SettingsController(requireContext());
        
        etName = view.findViewById(R.id.etName);
        etUsername = view.findViewById(R.id.etUsername);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            settingsController.updateUser(name, username, new SettingsController.UpdateCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    loadUserInfo(); // Reload to reflect changes
                }
                
                @Override
                public void onError(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        btnLogout.setOnClickListener(v -> {
            settingsController.logout(new SettingsController.LogoutCallback() {
                @Override
                public void onLogout() {
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
        });
        
        loadUserInfo();
    }
    
    private void loadUserInfo() {
        settingsController.loadUserInfo(new SettingsController.UserInfoCallback() {
            @Override
            public void onUserInfoLoaded(String name, String username, String avatarUri) {
                etName.setText(name);
                etUsername.setText(username);
            }
            
            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

