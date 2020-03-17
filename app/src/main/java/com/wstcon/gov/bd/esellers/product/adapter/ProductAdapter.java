package com.wstcon.gov.bd.esellers.product.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.category.categoryModel.Category;
import com.wstcon.gov.bd.esellers.database.DatabaseQuery;
import com.wstcon.gov.bd.esellers.interfaces.CategoryListener;
import com.wstcon.gov.bd.esellers.mainApp.adapter.HorizontalAdapter;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.RecyclerViewItem;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.utility.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.wstcon.gov.bd.esellers.utility.ImageHelper.getRoundedCornerBitmap;

public class ProductAdapter extends RecyclerView.Adapter {

    private String TAG="ProductAdapter ";

    private List<RecyclerViewItem> recyclerViewItems;

    // Item Type
    private static final int PRODUCT_ITEM = 0;

    private static final int CATEGORY_ITEM = 1;
    private Context context;


    public ProductAdapter(List<RecyclerViewItem> recyclerViewItems, Context context) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
       if (viewType == CATEGORY_ITEM) {
            row = inflater.inflate(R.layout.category_row, parent, false);
            return new CategoryHolder(row);
        }else if (viewType == PRODUCT_ITEM) {
            row = inflater.inflate(R.layout.vertical_row, parent, false);
            return new VerticalViewHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        if (holder instanceof VerticalViewHolder) {

            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;
            VerticalModel verticalModel = (VerticalModel) recyclerViewItem;
//            verticalViewHolder.catTV.setText(verticalModel.getHorizontalModels().get(0).getStatus());

            ArrayList<HorizontalModel> hmList = verticalModel.getHorizontalModels();

            Log.e(TAG, "onBindViewHolder: "+hmList.size() );
//            ArrayList<HorizontalModel> hmList2=new ArrayList<>();
//            for (int i=0; i<2;i++) {
//                HorizontalModel customlist=hmList.get(i);
//                hmList2.add(customlist);
//                Log.e(TAG, "onBindViewHolder: hmList2 size"+hmList2.size() );
//            }

            verticalViewHolder.horizontalRV.setHasFixedSize(true);
            HorizontalAdapter horizontalAdapter = new HorizontalAdapter(context, hmList);
            verticalViewHolder.horizontalRV.setLayoutManager(new GridLayoutManager(context,  2));
            verticalViewHolder.horizontalRV.setAdapter(horizontalAdapter);
        } else if (holder instanceof CategoryHolder) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;

        }
    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //if its header then return header item
       if (recyclerViewItem instanceof VerticalModel)
            return PRODUCT_ITEM;
        else if (recyclerViewItem instanceof Category)
            return CATEGORY_ITEM;
        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }


    private class VerticalViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView horizontalRV;
        private TextView catTV, seeTV;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRV = itemView.findViewById(R.id.horizontalRV);
            catTV = itemView.findViewById(R.id.catTV);
            seeTV = itemView.findViewById(R.id.seeMoreTV);


            horizontalRV.addItemDecoration(new VerticalSpaceItemDecoration(120));

            catTV.setVisibility(View.GONE);
            seeTV.setVisibility(View.GONE);
        }
    }


    private class CategoryHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
//        ImageView imageView;
        TextView textView;

        public CategoryHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.catImg);
            textView = itemView.findViewById(R.id.catTxt);
            linearLayout = itemView.findViewById(R.id.imgLayout);

            DatabaseQuery query=new DatabaseQuery(context);
            List<Category> categories=query.getCategory();
            for (Category c: categories){
                setCatIcon(c);
            }
        }

        public void setCatIcon(final Category category) {
            View view = LayoutInflater.from(context).inflate(R.layout.single_cat, null, false);
            ImageView imageView = view.findViewById(R.id.catImg);
            TextView textView = view.findViewById(R.id.catTxt);
            textView.setMaxLines(2);
            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            textView.setText(category.getCategoryName());
            Log.e(TAG, "setCatIcon: "+category.getCategoryName() );
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            parms.setMargins(20, 0, 20, 0);
            view.setLayoutParams(parms);

            imageView.setImageBitmap(getRoundedCornerBitmap(category.getBitmap(),120));
//            imageView.setImageBitmap(category.getBitmap());
            linearLayout.addView(view);
            imageView.setId(category.getId());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CategoryListener) context).onCatIconClick(category.getId());
                }
            });
        }
    }


    public void updateList(List<VerticalModel> products) {

        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

        Category category = new Category();

        recyclerViewItems.add(category);

        recyclerViewItems.addAll(products);

        this.recyclerViewItems = recyclerViewItems;

        Log.e("", "updateList: " + recyclerViewItems.size());
        notifyDataSetChanged();
    }


}
