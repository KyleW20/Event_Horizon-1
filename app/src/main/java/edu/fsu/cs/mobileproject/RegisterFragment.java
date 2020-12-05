package edu.fsu.cs.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.register_fragment,
                container, false);

        final EditText etName = view.findViewById(R.id.et_name);
        final EditText etEmail = view.findViewById(R.id.et_email);
        final EditText etPassword = view.findViewById(R.id.et_password);
        final EditText etRePassword = view.findViewById(R.id.et_repassword);
        Button btn_reg = view.findViewById(R.id.btn_register);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                SharedPreferences preferences = getActivity().
                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                String pName = etName.getText().toString();
                String pEmail = etEmail.getText().toString();
                String pPassword = etPassword.getText().toString();
                String pRePassword = etRePassword.getText().toString();

                if(TextUtils.isEmpty(pName)){
                    etName.setError("Required: Name");
                }
                else if(TextUtils.isEmpty(pEmail))
                {
                    etEmail.setError("Required: Email");
                }
                else if(TextUtils.isEmpty(pPassword))
                {
                    etPassword.setError("Required: Password");
                }else if(!pPassword.equals(pRePassword))
                {
                    etRePassword.setError("Passwords Do Not Match");
                }
                else
                {
                    Toast.makeText(getActivity(), "Go Login!", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(pEmail + pPassword + "data", pName);
                    editor.commit();
                }
            }
        });
        return view;
    }

}