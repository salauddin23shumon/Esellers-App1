package com.wstcon.gov.bd.esellers.mainApp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    private Context context;
    private ArrayList<HorizontalModel> hmList;
    private File imgFilePath;

    public HorizontalAdapter(Context context, ArrayList<HorizontalModel> hmList) {
        this.context = context;
        this.hmList = hmList;
        imgFilePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.column2, viewGroup, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HorizontalViewHolder horizontalViewHolder, int i) {

        final HorizontalModel horizontalModel = hmList.get(i);
        final String id =String.valueOf(horizontalModel.getProduct().getId());
        File dir = new File(imgFilePath+"/thumb/");
        final File thumbImageFile = new File(dir,  id + ".jpg");



        if (!dir.exists()){
            dir.mkdir();
        }

        if (!thumbImageFile.exists()) {
            Glide.with(context)
                    .asBitmap()
                    .load(BASE_URL+horizontalModel.getProduct().getProductImage())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            horizontalViewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            horizontalViewHolder.progressBar.setVisibility(View.GONE);
                            try {
                                FileOutputStream outputStreamthumb = new FileOutputStream(thumbImageFile);
//                        Bitmap thumbBitmap = Bitmap.createScaledBitmap(loadedImage,300,400,false);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStreamthumb);
                        Toast.makeText(context, id+" thumb saved", Toast.LENGTH_SHORT).show();
                                Log.d("file", ": " + id + " saved");
                                outputStreamthumb.flush();
                                outputStreamthumb.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }).into(horizontalViewHolder.imageView);
        }else {
            Log.e("else", "onBindViewHolder: "+imgFilePath+"/thumb/" );
            Log.e("else", "onBindViewHolder: "+dir.toString() );
            Bitmap bitmap = BitmapFactory.decodeFile(dir.getPath() + "/" + id +".jpg");
            horizontalViewHolder.progressBar.setVisibility(View.GONE);
            horizontalViewHolder.imageView.setImageBitmap(bitmap);
        }

        horizontalViewHolder.productTV.setText(horizontalModel.getProduct().getProductName());
        horizontalViewHolder.priceTV.setText(horizontalModel.getProduct().getProductPrice());
        horizontalViewHolder.ratingBar.setRating(Float.parseFloat(horizontalModel.getProduct().getRating()));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha);
        horizontalViewHolder.imageView.startAnimation(animation);

        horizontalViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fullView(horizontalModel);
//                context.startActivity(new Intent(context, ProductDetailsActivity.class).putExtra("item", horizontalModel));
            }
        });

        horizontalViewHolder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!horizontalModel.getProduct().isAddedToCart())
                {
                    horizontalModel.getProduct().setAddedToCart(true);
                    horizontalViewHolder.cartBtn.setText("Remove");
                    if(context instanceof MainActivity)
                    {
                        ((AddorRemoveCallbacks)context).onAddProduct();
                    }

                }
                else
                {
                    horizontalModel.getProduct().setAddedToCart(false);
                    horizontalViewHolder.cartBtn.setText("Add");
                    ((AddorRemoveCallbacks)context).onRemoveProduct();
                }
            }
        });
    }

    private void fullView(HorizontalModel horizontalModel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View fullView = inflater.inflate(R.layout.fullscreeen, null, false);
        final Dialog fullScreenDilog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        fullScreenDilog.setContentView(fullView);

        Button closeBtn = fullScreenDilog.findViewById(R.id.btnClose);
        ImageView fullScreenView = fullScreenDilog.findViewById(R.id.fullView);
//        Log.e("", "fullView: " + horizontalModel.getProduct().getImage());
//        fullScreenView.setImageResource(horizontalModel.getImage());
//        Glide.with(context).load(horizontalModel.getImgUrl()).into(fullScreenView);
//        Glide.with(context).load(horizontalModel.getProduct().getImage()).centerCrop().into(fullScreenView);
//        Picasso.get().load(horizontalModel.getImgUrl()).fit().centerCrop().into(fullScreenView);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDilog.dismiss();
            }
        });

        fullScreenDilog.show();
    }


    @Override
    public int getItemCount() {
        Log.e("", "getItemCount: " + hmList.size());
        return hmList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private TextView productTV, priceTV;
        private ImageView imageView, wishBtn;
        private ProgressBar progressBar;
        private Button cartBtn;
        private RatingBar ratingBar;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            productTV = itemView.findViewById(R.id.productTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            imageView = itemView.findViewById(R.id.imageview);
            progressBar = itemView.findViewById(R.id.progress);
            cartBtn = itemView.findViewById(R.id.cartBtn);
            wishBtn = itemView.findViewById(R.id.wishBtn);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }


    public interface HorizontalAdapterAction{

    }
}
