package com.example.productprmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.productprmapp.R;
import com.example.productprmapp.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductModel> productList;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);
        holder.textViewTen.setText(product.getProductName());
        holder.textViewGia.setText(String.format("%,dđ", (int) product.getPrice()));

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ProductDetail.class);
//            intent.putExtra("productId", product.get_id());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTen, textViewGia, textViewDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTen = itemView.findViewById(R.id.textViewTen);
            textViewGia = itemView.findViewById(R.id.textViewGia);
//            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
