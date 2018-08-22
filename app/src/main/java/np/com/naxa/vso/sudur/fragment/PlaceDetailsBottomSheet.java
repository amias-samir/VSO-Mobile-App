package np.com.naxa.vso.sudur.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.local.Bussiness;


public class PlaceDetailsBottomSheet extends BottomSheetDialogFragment {
    Bussiness bussiness;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.backdrop)
    ImageView backdrop;


    public static PlaceDetailsBottomSheet getInstance(Bussiness bussiness) {

        PlaceDetailsBottomSheet placeDetailsBottomSheet = new PlaceDetailsBottomSheet();
        placeDetailsBottomSheet.setObject(bussiness);
        return placeDetailsBottomSheet;
    }

    private void setObject(Bussiness bussiness) {
        this.bussiness = bussiness;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_custom_bottom_sheet, container, false);
        ButterKnife.bind(this, rootView);

        title.setText(bussiness.getBusinessName());
        address.setText(bussiness.getBusinessAddress());
        desc.setText(bussiness.getBusinessDescription());
        Glide.with(this).load(bussiness.getPhotoPath()).into(backdrop);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
