package com.wstcon.gov.bd.esellers.product;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.CartActivity;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.utility.Converter;

import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cart_count;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.globalCartList;
import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;

public class ProductDetailsActivity extends AppCompatActivity implements AddorRemoveCallbacks{

    private static final String TAG = "ProductDetailsActivity ";
    private TextView productTV, priceTV, shortDecTV, longDescTV;
    private ImageView productIV, brandIV;
    private Button cartBtn, buyBtn;
    private Toolbar toolbar;
    private RatingBar ratingBar;
    private HorizontalModel horizontalModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        horizontalModel= (HorizontalModel) getIntent().getSerializableExtra("product");

        productTV=findViewById(R.id.nameTV);
        priceTV=findViewById(R.id.priceTV);
        shortDecTV=findViewById(R.id.desc1ET);
        longDescTV=findViewById(R.id.desc2ET);
        productIV=findViewById(R.id.productIV);
        brandIV=findViewById(R.id.brandIV);
        cartBtn=findViewById(R.id.cartBtn);
        buyBtn=findViewById(R.id.buyBtn);
        ratingBar=findViewById(R.id.rating);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Product Details");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart();
                cart.setProductId(horizontalModel.getProduct().getId());
                cart.setProductName(horizontalModel.getProduct().getProductName());
                cart.setProductImg(horizontalModel.getProduct().getProductImage());
                cart.setProductQuantity(1);
                cart.setSize("");
                cart.setColor("");
                cart.setProductPrice(Double.parseDouble(horizontalModel.getProduct().getProductPrice()));
                cart.setTotalCash(Double.parseDouble(horizontalModel.getProduct().getProductPrice()));

                if (!horizontalModel.getProduct().isAddedToCart()) {
                    horizontalModel.getProduct().setAddedToCart(true);
//                    horizontalViewHolder.cartBtn.setText("Remove");
                    onAddProduct(cart);

                } else {
                    Toast.makeText(ProductDetailsActivity.this, "already added into cart", Toast.LENGTH_SHORT).show();
                    horizontalModel.getProduct().setAddedToCart(false);
//                    horizontalViewHolder.cartBtn.setText("Add To Cart");
//                    ((AddorRemoveCallbacks) context).onRemoveProduct(cart.getProductId());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductDetailsActivity.this, cart_count, R.drawable.cart3));
        MenuItem menuItem2 = menu.findItem(R.id.notification_action);
        menuItem2.setIcon(Converter.convertLayoutToImage(ProductDetailsActivity.this, 0, R.drawable.ic_notifications_white_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void fullView(HorizontalModel horizontalModel) {
        LayoutInflater inflater=LayoutInflater.from(this);
        View fullView= inflater.inflate(R.layout.fullscreeen,null,false);
        final Dialog fullScreenDilog=new Dialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
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

    @Override
    public void onAddProduct(Cart cart) {
        cart_count++;
        invalidateOptionsMenu();
        globalCartList.add(cart);
        Log.e(TAG, "onAddProduct: " + globalCartList.size() + " " + cart.getProductName());
        Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveProduct(int id) {

    }
}
