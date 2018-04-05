package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.farm.farm2fork.CustomViews.customspinner.MaterialSpinner;
import com.farm.farm2fork.Interface.ImagePathListener;
import com.farm.farm2fork.Interface.LocationSetListener;
import com.farm.farm2fork.Models.CropNameModel;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.data.UserDataManager;
import com.farm.farm2fork.ui.mainfarmscreen.FarmScreen;
import com.kbeanie.multipicker.api.ImagePicker;
import com.schibstedspain.leku.LocationPickerActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.farm.farm2fork.Utils.Constants.BASE_URL;

/**
 * Created by master on 10/3/18.
 */

public class AddFarmFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_add_feed, container, false);


        ed_crop = view.findViewById(R.id.ed_crop);
        userDataManager = new UserDataManager(mContext);
        btn_add = view.findViewById(R.id.add);
        txt_location = view.findViewById(R.id.txt_location);
        cameraicon = view.findViewById(R.id.cameraicon);
        mainimage = view.findViewById(R.id.photo);
        ed_size = view.findViewById(R.id.ed_size);

        ((FarmScreen) mContext).setToolbarTitle("Add Farm");

        compositeDisposable = new CompositeDisposable();

        setCropList();

        setupFarmSizeUnitsSpinner();


        view.findViewById(R.id.rootphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmScreen) mContext).showImageChoosingDialog();
            }
        });


        ((FarmScreen) mContext).setonImagePathListener(new ImagePathListener() {
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


        ((FarmScreen) mContext).setonLocationSetByUser(new LocationSetListener() {

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
                if (locationtaken && ed_crop.getText().toString().trim().length() != 0 && ed_size.getText().toString().trim().length() != 0)
                    makeServerReq();
                else
                    Toast.makeText(mContext, "Please fill up all details", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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

    private void setCropList() {
        List<CropNameModel> cropNameList = CropNameModel.listAll(CropNameModel.class);

        getCropListObservable(cropNameList).subscribe(getCropListObserver());

    }

    private void updateCropEditText(List<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strings);
        ed_crop.setAdapter(adapter);
    }


    private void makeServerReq() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Adding Farm! Please Wait");
        progressDialog.show();
        Log.d(TAG, "makeServerReq: " + ed_size.getText().toString().trim());
        Log.d(TAG, "makeServerReq: " + ed_crop.getText().toString());
        Log.d(TAG, "makeServerReq: " + mlocationInfoModel.getCity());


        if (farmSizeUnit.equals("bigha")) {
            farmsize_acre = String.valueOf(Float.valueOf(ed_size.getText().toString().trim()) * 0.625);

        } else if (farmSizeUnit.equals("acre")) {
            farmsize_acre = ed_size.getText().toString().trim();


        } else if (farmSizeUnit.equals("hectare")) {
            farmsize_acre = String.valueOf(Float.valueOf(ed_size.getText().toString().trim()) * 2.471);

        } else if (farmSizeUnit.equals("guntha")) {
            farmsize_acre = String.valueOf(Float.valueOf(ed_size.getText().toString().trim()) * 0.025);


        }

        AndroidNetworking.post(BASE_URL + "addfarm_temp.php")
                .addBodyParameter("crop", ed_crop.getText().toString().trim())
                .addBodyParameter("farmsize", ed_size.getText().toString().trim() + " " + farmSizeUnit)
                .addBodyParameter("farmsize_acre", farmsize_acre)
                .addBodyParameter("image", imageencoded)
                .addBodyParameter("loc_address", mlocationInfoModel.getAddress())
                .addBodyParameter("loc_lat", mlocationInfoModel.getLatitude())
                .addBodyParameter("loc_long", mlocationInfoModel.getLongitude())
                .addBodyParameter("postalCode", mlocationInfoModel.getPostalzipcode())
                .addBodyParameter("loc_city", mlocationInfoModel.getCity())
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
                            ((FarmScreen) mContext).showMainScreen();
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
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onDestroy();


    }


    public Observable<List<String>> getCropListObservable(List<CropNameModel> cropNameList) {
        return Observable.just(cropNameList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<List<CropNameModel>, List<String>>() {
            @Override
            public List<String> apply(List<CropNameModel> cropNameModels) throws Exception {
                List<String> croplistString = new ArrayList<>();
                for (CropNameModel cropNameModel : cropNameModels) {
                    String cropname = cropNameModel.getName();
                    croplistString.add(cropname);
                }
                return croplistString;
            }
        });
    }

    public Observer<? super List<String>> getCropListObserver() {
        return new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<String> strings) {
                updateCropEditText(strings);

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
