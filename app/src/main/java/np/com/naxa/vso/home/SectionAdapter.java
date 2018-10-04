package np.com.naxa.vso.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.home.model.MapDataCategory;

public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        MapDataCategory mapDataCategory = (MapDataCategory) item.t;
        switch (helper.getLayoutPosition() %
                2) {
            case 0:
//                helper.setImageResource(R.id.iv, mapDataCategory.getImage());



                io.reactivex.Observable.just(mapDataCategory.image)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(String s) {
//                                try {
//                                    Bitmap theBitmap = Glide.
//                                            with(VSO.getInstance()).
//                                            load(s).
//                                            asBitmap().
//                                            into(100, 100). // Width and height
//                                            get();
//                                    helper.setImageBitmap(R.id.iv, theBitmap);
//
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });


                break;
            case 1:
//                helper.setImageResource(R.id.iv, mapDataCategory.getImage());



                io.reactivex.Observable.just(mapDataCategory.image)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(String s) {
//                                try {
//                                    Bitmap theBitmap = Glide.
//                                            with(VSO.getInstance()).
//                                            load(s).
//                                            asBitmap().
//                                            into(100, 100). // Width and height
//                                            get();
//                                    helper.setImageBitmap(R.id.iv, theBitmap);
//
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });



                break;


        }

        helper.addOnClickListener(R.id.card_view);
        helper.setText(R.id.tv_title, mapDataCategory.getName());
        helper.setText(R.id.tv_subtitle, String.valueOf(new Random().nextInt(50) + 1));
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);


    }


}