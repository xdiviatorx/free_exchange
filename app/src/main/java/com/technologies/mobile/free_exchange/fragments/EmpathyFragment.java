package com.technologies.mobile.free_exchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.technologies.mobile.free_exchange.R;

/**
 * Created by diviator on 26.12.2016.
 */

public class EmpathyFragment extends Fragment {

    public static final String POSITION = "POSITION";

    int mPos = 0;

    ImageView ivBackground;
    ImageView ivIcon;
    TextView tvBigText;
    TextView tvText;
    Button bButton;

    public static Fragment getInstance(Bundle args){
        Fragment empathyFragment = new EmpathyFragment();
        empathyFragment.setArguments(args);
        return empathyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPos = getArguments().getInt(POSITION,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empathy,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    protected void initViews(View view){
        ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        tvBigText = (TextView) view.findViewById(R.id.tvBigText);
        tvText = (TextView) view.findViewById(R.id.tvText);
        bButton = (Button) view.findViewById(R.id.bButton);
        switch (mPos){
            case 2:{
                ivBackground.setImageResource(R.color.colorEmpathy2);
                ivIcon.setImageResource(R.drawable.empathy_icon2);
                tvBigText.setText(R.string.emp2_slogan);
                tvText.setText(R.string.emp2_text);
                bButton.setText(R.string.emp2_button);
                break;
            }
            case 3:{
                ivBackground.setImageResource(R.color.colorEmpathy3);
                ivIcon.setImageResource(R.drawable.empathy_icon3);
                tvBigText.setText(R.string.emp3_slogan);
                tvText.setText(R.string.emp3_text);
                bButton.setText(R.string.emp3_button);
                break;
            }
            case 4:{
                ivBackground.setImageResource(R.color.colorEmpathy4);
                ivIcon.setImageResource(R.drawable.empathy_icon4);
                tvBigText.setText(R.string.emp4_slogan);
                tvText.setText(R.string.emp4_text);
                bButton.setText(R.string.emp4_button);
                break;
            }
        }
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
}
