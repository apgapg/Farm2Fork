package com.farm.farm2fork.ui.farmscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.CustomViews.customspinner.MaterialSpinner;
import com.farm.farm2fork.Interface.ImagePathListener;
import com.farm.farm2fork.Interface.LocationSetListener;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.data.UserDataManager;
import com.kbeanie.multipicker.api.ImagePicker;
import com.schibstedspain.leku.LocationPickerActivity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by master on 10/3/18.
 */

public class AddFarmFragment extends Fragment implements FarmContract.AddFarmFragmentView {
    private static final String TAG = AddFarmFragment.class.getName();
    private Activity mContext;
    private Button btn_add;
    private TextView txt_location;
    private UserDataManager userDataManager;
    private LocationInfoModel mlocationInfoModel;
    private EditText ed_size;
    private AutoCompleteTextView ed_crop;

    private boolean locationtaken = false;
    private ImageView cameraicon, mainimage;
    private ImagePicker imagePicker;
    private String imagepath = "";
    private String imageencoded = "";
    private String farmSizeUnit = "acre";
    private String farmsize_acre;
    private Disposable disposable;
    private Object cropListObservable;
    private Observer<? super List<String>> cropListObserver;
    private CompositeDisposable compositeDisposable;
    private View view;
    private AddFarmFragmentPresentor mAddFarmFragmentPresentor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_add_farm, container, false);


        ed_crop = view.findViewById(R.id.ed_crop);
        userDataManager = new UserDataManager(mContext);
        btn_add = view.findViewById(R.id.add);
        txt_location = view.findViewById(R.id.txt_location);
        cameraicon = view.findViewById(R.id.cameraicon);
        mainimage = view.findViewById(R.id.photo);
        ed_size = view.findViewById(R.id.ed_size);

        ((FarmActivity) mContext).setToolbarTitle("Add Farm");

        mAddFarmFragmentPresentor = new AddFarmFragmentPresentor(((ApplicationClass) getActivity().getApplication()).getmAppDataManager());
        mAddFarmFragmentPresentor.setView(this);

        mAddFarmFragmentPresentor.loadCropList();

        setupFarmSizeUnitsSpinner();


        view.findViewById(R.id.rootphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmActivity) mContext).showImageChoosingDialog();
            }
        });


        ((FarmActivity) mContext).setonImagePathListener(new ImagePathListener() {
            @Override
            public void onImagePath(String queryUri) {
                imagepath = queryUri;
                Glide.with(mContext).load(queryUri).into(mainimage);
                cameraicon.setVisibility(View.INVISIBLE);
                if (!imagepath.isEmpty()) {
                    Glide.with(mContext).load(imagepath).asBitmap().toBytes(Bitmap.CompressFormat.JPEG, 90).format(DecodeFormat.PREFER_ARGB_8888).atMost().override(1500, 1500).into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            imageencoded = Base64.encodeToString(resource, Base64.DEFAULT);
                        }
                    });

                }
            }
        });


        ((FarmActivity) mContext).setonLocationSetByUser(new LocationSetListener() {

            @Override
            public void onLocationSetByUser(LocationInfoModel locationInfoModel) {
                txt_location.setText(locationInfoModel.getAddress());
                mlocationInfoModel = locationInfoModel;
                locationtaken = true;
            }
        });

        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withGooglePlacesEnabled()
                        .withGeolocApiKey("AIzaSyBtuQ0bOdBshWBziK31gyUY2wKFnQnrEyc")
                        .withSearchZone("en_in")
                        .shouldReturnOkOnBackPressed()
                        .withSatelliteViewHidden()
                        .build(mContext);

                mContext.startActivityForResult(intent, 1);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmAddFarmFragmentPresentor().addFarm(locationtaken, ed_crop.getText().toString().trim(), ed_size.getText().toString().trim(), farmSizeUnit, imageencoded, mlocationInfoModel);
            }
        });
        return view;
    }

    public AddFarmFragmentPresentor getmAddFarmFragmentPresentor() {
        return mAddFarmFragmentPresentor;
    }

    private void setupFarmSizeUnitsSpinner() {
        MaterialSpinner spinner = view.findViewById(R.id.spinner);
        spinner.setItems("acre", "hectare", "bigha", "guntha");
        spinner.setSelectedIndex(0);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                farmSizeUnit = item;
            }
        });
    }


    private void updateCropEditText(List<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strings);
        ed_crop.setAdapter(adapter);
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

    @Override
    public void OnCropListFetch(List<String> cropList) {
        updateCropEditText(cropList);
    }

    @Override
    public void onValidationError() {
        Toast.makeText(mContext.getApplicationContext(), "Please fill up all details", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgressBar() {
        ((FarmActivity) mContext).showProgressBar();
    }

    @Override
    public void onAddFarmReqFail() {
        Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddFarmReqSuccess() {
        Toast.makeText(mContext.getApplicationContext(), "Farm Successfully Added!", Toast.LENGTH_SHORT).show();
        ((FarmActivity) mContext).showBackFarmFragment();

    }

    @Override
    public void hideProgressBar() {
        ((FarmActivity) mContext).hideProgressBar();

    }
}
