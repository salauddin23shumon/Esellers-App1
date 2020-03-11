package com.wstcon.gov.bd.esellers.cart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;
    private Context context;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_fragment,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, priceTV, quantityTV;
        private Button  incTV, decTV;
        private ImageView productImg;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTV);
            priceTV=itemView.findViewById(R.id.priceTV);
            quantityTV=itemView.findViewById(R.id.quantityTV);
            incTV=itemView.findViewById(R.id.incrementBtn);
            decTV=itemView.findViewById(R.id.decrementBtn);
            productImg=itemView.findViewById(R.id.productImg);

        }
    }
}
