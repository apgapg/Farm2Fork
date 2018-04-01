package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.farm.data.UserDataManager;
import com.farm.farm2fork.Interface.ImagePathListener;
import com.farm.farm2fork.R;
import com.farm.farm2fork.activity.MainNavScreen;
import com.kbeanie.multipicker.api.ImagePicker;

/**
 * Created by master on 10/3/18.
 */

public class AddFeedFragment extends Fragment {
    public static final String BASE_URL = "https://www.reweyou.in/fasalapp/";
    private static final String TAG = AddFeedFragment.class.getName();
    private Activity mContext;
    private Button btn_add;
    private TextView txt_crop;
    private UserDataManager userDataManager;
    private ImageView cameraicon, mainimage;
    private ImagePicker imagePicker;
    private String imagepath = "";
    private String imageencoded = "";
    private EditText ed_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_feed, container, false);
        ((MainNavScreen) mContext).setToolbarTitle("Add Post");

        userDataManager = new UserDataManager(mContext);
        btn_add = view.findViewById(R.id.add);
        txt_crop = view.findViewById(R.id.txt_crop);
        ed_description = view.findViewById(R.id.ed_des);
        cameraicon = view.findViewById(R.id.cameraicon);
        mainimage = view.findViewById(R.id.photo);

        txt_crop.setText(getArguments().getString("crop"));
        txt_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Crop cannot be changed!", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.rootphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainNavScreen) mContext).checkstoragepermissionforimage();
            }
        });

        ((MainNavScreen) mContext).setonImagePathListener(new ImagePathListener() {
            @Override
            public void onImagePath(String queryUri) {
                imagepath = queryUri;
                Glide.with(mContext).load(queryUri).into(mainimage);
                cameraicon.setVisibility(View.INVISIBLE);
                if (!imagepath.isEmpty()) {
                    Glide.with(mContext).load(imagepath).asBitmap().toBytes(Bitmap.CompressFormat.JPEG, 90).format(DecodeFormat.PREFER_ARGB_8888).atMost().override(1200, 1200).into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            imageencoded = Base64.encodeToString(resource, Base64.DEFAULT);
                        }
                    });

                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_description.getText().toString().trim().length() != 0)
                    makeServerReq();
                else
                    Toast.makeText(mContext, "Please fill up all details", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void makeServerReq() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Adding Farm! Please Wait");
        progressDialog.show();
        Log.d(TAG, "makeServerReq: " + ed_description.getText().toString().trim());


        AndroidNetworking.post(BASE_URL + "addfeeds.php")
                .addBodyParameter("crop", getArguments().getString("crop"))
                .addBodyParameter("description", ed_description.getText().toString().trim())
                .addBodyParameter("image", imageencoded)
                .addBodyParameter("city", getArguments().getString("city"))
                .addBodyParameter("name", "Ayush P Gupta")
                .addBodyParameter("uid", userDataManager.getUid())
                .addBodyParameter("authtoken", userDataManager.getAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        Log.d(TAG, "onResponse: " + response);
                        if (response.contains("Successfully Uploaded")) {
                            ((MainNavScreen) mContext).showMainScreen();
                        } else
                            Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError);
                        progressDialog.cancel();
                        Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            mContext = (Activity) context;
        else throw new IllegalArgumentException("Context should be an instance of Activity");
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();

    }


}
