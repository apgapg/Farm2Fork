package com.farm.farm2fork.ui.farmscreen;

import com.farm.farm2fork.Models.CropNameModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by master on 7/4/18.
 */

public class ObservableHelper {
    private final CompositeDisposable mCompositeDisposable;
    private CropListFetchListener mCropListFetchListener;

    public ObservableHelper() {

        mCompositeDisposable = new CompositeDisposable();
    }

    public void getCropList(CropListFetchListener cropListFetchListener) {
        this.mCropListFetchListener = cropListFetchListener;
        List<CropNameModel> cropNameList = CropNameModel.listAll(CropNameModel.class);
        getCropListObservable(cropNameList).subscribe(getCropListObserver());

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
            }

            @Override
            public void onNext(List<String> cropList) {
                mCropListFetchListener.onCropListFetch(cropList);
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

    public void onUnsubscribe() {

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    public interface CropListFetchListener {

        void onCropListFetch(List<String> cropList);
    }
}
