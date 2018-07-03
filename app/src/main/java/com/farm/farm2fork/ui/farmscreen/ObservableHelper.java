package com.farm.farm2fork.ui.farmscreen;


import com.farm.farm2fork.models.CropNameModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by master on 7/4/18.
 */

public class ObservableHelper {
    private CropListFetchListener mCropListFetchListener;

    public ObservableHelper() {

    }

    public void getCropList(CropListFetchListener cropListFetchListener) {
        this.mCropListFetchListener = cropListFetchListener;
        List<CropNameModel> cropNameList = CropNameModel.listAll(CropNameModel.class);
        getCropListObservable(cropNameList).subscribe(getCropListObserver());

    }

    public Observable<List<String>> getCropListObservable(List<CropNameModel> cropNameList) {
        return Observable.just(cropNameList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<List<CropNameModel>, List<String>>() {
            @Override
            public List<String> call(List<CropNameModel> cropNameModels) {
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
            public void onNext(List<String> cropList) {
                mCropListFetchListener.onCropListFetch(cropList);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }


        };
    }

    public void onUnsubscribe() {


    }

    public interface CropListFetchListener {

        void onCropListFetch(List<String> cropList);
    }
}
