package com.wstcon.gov.bd.esellers.product;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;

import java.util.Objects;

import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {

    private TextView productTV, priceTV, shortDecTV, longDescTV;
    private ImageView productIV, brandIV;
    private Button cartBtn, buyBtn;
    private RatingBar ratingBar;
    private HorizontalModel horizontalModel;
    private Context context;
    private Toolbar toolbar;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            horizontalModel= (HorizontalModel) bundle.getSerializable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_details, container, false);

        productTV=view.findViewById(R.id.nameTV);
        priceTV=view.findViewById(R.id.priceTV);
        shortDecTV=view.findViewById(R.id.desc1ET);
        longDescTV=view.findViewById(R.id.desc2ET);
        productIV=view.findViewById(R.id.productIV);
        brandIV=view.findViewById(R.id.brandIV);
        cartBtn=view.findViewById(R.id.cartBtn);
        buyBtn=view.findViewById(R.id.buyBtn);
        ratingBar=view.findViewById(R.id.rating);
        toolbar=view.findViewById(R.id.toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        productTV.setText(horizontalModel.getProduct().getProductName());
        priceTV.setText(horizontalModel.getProduct().getProductPrice());
        Glide.with(this).load(BASE_URL+horizontalModel.getProduct().getProductImage()).into(productIV);

        productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullView(horizontalModel);
            }
        });
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }


    private void fullView(HorizontalModel horizontalModel) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View fullView= inflater.inflate(R.layout.fullscreeen,null,false);
        final Dialog fullScreenDilog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        fullScreenDilog.setContentView(fullView);

        Button closeBtn=fullScreenDilog.findViewById(R.id.btnClose);
//        ImageView fullScreenView=fullScreenDilog.findViewById(R.id.fullView);
        PhotoView fullScreenView=fullScreenDilog.findViewById(R.id.fullView);
//        Log.e("", "fullView: "+horizontalModel.getProduct().getImage() );
//        fullScreenView.setImageResource(horizontalModel.getImage());
//        Glide.with(context).load(horizontalModel.getImgUrl()).into(fullScreenView);
        Glide.with(this).load(BASE_URL+horizontalModel.getProduct().getProductImage()).into(fullScreenView);
//        Picasso.get().load(horizontalModel.getImgUrl()).fit().centerCrop().into(fullScreenView);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDilog.dismiss();
            }
        });

        fullScreenDilog.show();
    }

}
