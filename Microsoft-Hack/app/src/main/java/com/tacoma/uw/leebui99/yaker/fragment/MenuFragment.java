package com.tacoma.uw.leebui99.yaker.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import com.tacoma.uw.leebui99.yaker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private LauncherMenuListener mLauncherMenuListener;
    private TextView mTittle;
    private Button mColor;
    private Button mPic;

    public interface LauncherMenuListener{
        public void launchMenu();
    }
    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_menu, container, false);
        mTittle = (TextView) v.findViewById(R.id.tittle);

        mPic = (Button) v.findViewById(R.id.picture);
        mColor = (Button) v.findViewById(R.id.color);
        mColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ColorActivity.class);
                Bundle bundle = new Bundle();
                startActivity(myIntent);
            }
        });


        return v;
    }


}
