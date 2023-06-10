package com.tanpanama.h2ohub.NewContainer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.tanpanama.h2ohub.R;
import com.tanpanama.h2ohub.StepView.StepView;

import es.dmoral.toasty.Toasty;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewContainer4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewContainer4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private int empty_weight = 0;
    private int full_weight = 0;

    private ImageView iv;
    private Button next;
    private Uri fileUri;

    public NewContainer4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewContainer4.
     */
    // TODO: Rename and change types and number of parameters
    public static NewContainer4 newInstance(String param1, String param2) {
        NewContainer4 fragment = new NewContainer4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            empty_weight = getArguments().getInt("empty_weight");
            full_weight = getArguments().getInt("full_weight");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_container4, container, false);

        iv = v.findViewById(R.id.ivImage);
        next = v.findViewById(R.id.btnNext);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                ImagePicker.Companion.with(getActivity())
                        .cameraOnly()
                        .cropSquare()
                        .maxResultSize(1080, 1080)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent Intent) {
                                startForProfileImageResult.launch(Intent);
                                return null;
                            }
                        });;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepView fs = (StepView) getFragmentManager().findFragmentById(R.id.StepView_Container);
                fs.nextStep();

                Bundle bundle = new Bundle();
                bundle.putInt("empty_weight", empty_weight);
                bundle.putInt("full_weight", full_weight);
                String path = fileUri.toString();
                bundle.putString("image", path);

                NewContainer5 nc = new NewContainer5();
                nc.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.Inner_Container, nc).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private ActivityResultLauncher<Intent> startForProfileImageResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK) {
                        fileUri = data.getData();
                        iv.setImageURI(fileUri);
                        if(!next.isEnabled()){
                            next.setEnabled(true);
                            next.setBackgroundColor(getResources().getColor(R.color.Green));
                            next.setTextColor(getResources().getColor(R.color.DarkGreen));
                        }
                    }
                }
            });
}