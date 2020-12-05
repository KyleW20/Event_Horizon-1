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

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login,
                container, false);


        final EditText et_Email = view.findViewById(R.id.et_email);
        final EditText et_Password = view.findViewById(R.id.et_password);
        final TextView tv_noLogin = view.findViewById(R.id.noLog);
        Button btn_log =  view.findViewById(R.id.btn_login);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String p_Email = et_Email.getText().toString();
                String p_Password = et_Password.getText().toString();

                SharedPreferences preferences = getActivity().
                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                String loginData = preferences.getString(p_Email + p_Password + "data",
                        "Incorrect");

                if(!loginData.equals("Incorrect"))
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", loginData);
                    editor.commit();

                    Intent intent = new Intent("LOGIN");
                    getActivity().sendBroadcast(intent);
                }else if(TextUtils.isEmpty(p_Email))
                {
                    et_Email.setError("Required: Email");
                }
                else if(TextUtils.isEmpty(p_Password))
                {
                    et_Password.setError("Required: Password");
                }
                else
                {
                    tv_noLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

}