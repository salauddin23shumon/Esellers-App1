package com.wstcon.gov.bd.esellers.order;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.order.orderModel.CustomerOrder;
import com.wstcon.gov.bd.esellers.order.orderModel.Detail;
import com.wstcon.gov.bd.esellers.order.orderModel.OrderDetails;
import com.wstcon.gov.bd.esellers.order.orderModel.Payment;
import com.wstcon.gov.bd.esellers.order.orderModel.Shipping;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    private static final String TAG = "OrderDetailsFragment ";
    private OrderDetails orderDetails;
    private Context context;
    private Toolbar toolbar;
    private TextView customerTV, orderIdTV, dateTV, statusTV, totalTV, dueTV, paidTV;
    private RecyclerView itemRV;
    private OrderItemAdapter adapter;
    private List<Detail> details;
    private CustomerOrder customerOrder;
    private Shipping shipping;
    private Payment payment;
    private BackToOrderList backToOrderList;


    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        backToOrderList = (BackToOrderList) context;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderDetails = (OrderDetails) bundle.getSerializable("details");
            if (orderDetails.getDetails() != null)
                details = orderDetails.getDetails();
            if (orderDetails.getPayment() != null)
                payment = orderDetails.getPayment();
            if (orderDetails.getShipping() != null)
                shipping = orderDetails.getShipping();
            customerOrder = orderDetails.getOrder();
            Log.e(TAG, "onAttach: " + orderDetails.getOrder().getId());
            Log.e(TAG, "onAttach: " + orderDetails.getDetails().size());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        customerTV = view.findViewById(R.id.customerTV);
        orderIdTV = view.findViewById(R.id.order_idTV);
        dateTV = view.findViewById(R.id.dateTV);
        statusTV = view.findViewById(R.id.statusTV);
        totalTV = view.findViewById(R.id.totalTV);
        dueTV = view.findViewById(R.id.dueTV);
        paidTV = view.findViewById(R.id.paidTV);
        itemRV = view.findViewById(R.id.itemRV);
//        toolbar=view.findViewById(R.id.toolbar);

        adapter = new OrderItemAdapter(context, details);
        itemRV.setHasFixedSize(true);
        itemRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        itemRV.setAdapter(adapter);

        customerTV.setText("Customer Name: " + shipping.getReceiverName());
        orderIdTV.setText("Order Id: " + String.valueOf(customerOrder.getId()));
//        dateTV.setText("Order Date: " + customerOrder.getCreatedAt());
        statusTV.setText("Order Status: " + customerOrder.getOrderStatus());

        totalTV.setText("Total: " + customerOrder.getOrderTotal());
        dueTV.setText("Due: 0.00");
        paidTV.setText("Paid: " + customerOrder.getOrderTotal());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order History");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((BackBtnPress)context).onBackBtnPress();
                backToOrderList.onBackToListClick();
            }
        });
    }

    public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {

        private Context context;
        private List<Detail> details;

        public OrderItemAdapter(Context context, List<Detail> details) {
            this.context = context;
            this.details = details;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_item_row, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Detail itemDetail = details.get(position);
            holder.idTV.setText(String.valueOf(position));
            holder.itemTV.setText(itemDetail.getProducts().getProductName());
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "item: " + details.size());
            return details.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView idTV, itemTV, quantityTV, priceTV;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                idTV = itemView.findViewById(R.id.order_idTV);
                itemTV = itemView.findViewById(R.id.productTV);
                quantityTV = itemView.findViewById(R.id.quantityTV);
                priceTV = itemView.findViewById(R.id.priceTV);
            }
        }
    }

    public interface BackToOrderList {
        void onBackToListClick();
    }
}
