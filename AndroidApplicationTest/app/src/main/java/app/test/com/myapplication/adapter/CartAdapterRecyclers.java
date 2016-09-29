package app.test.com.myapplication.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

import app.test.com.myapplication.R;
import app.test.com.myapplication.Utilities.CheckNetworkConnection;
import app.test.com.myapplication.Utilities.RoundedCornersTransformation;
import app.test.com.myapplication.database.ProductDatabaseHelper;
import app.test.com.myapplication.model.Product;

/**
 * Created by anupama.shelke on 9/29/2016.
 * This Adapter used for showing the product which are added into cart .
 *
 */
public class CartAdapterRecyclers extends RecyclerView.Adapter<CartAdapterRecyclers.ViewHolder>implements View.OnClickListener {

private final Context context;
private final List<Product> productList;
final int radius = 10;
final int margin = 0;
    private boolean networkFlag = false;
final Transformation transformation = new RoundedCornersTransformation(radius, margin, RoundedCornersTransformation.CornerType.ALL);
private ProductDatabaseHelper db;
    private static ClickListener clickListener;

    public CartAdapterRecyclers(Context activity, List<Product> List) {
        this.context = activity;
        this.productList = List;
        db = new ProductDatabaseHelper(context);
        if (CheckNetworkConnection.isConnectionAvailable(context.getApplicationContext())) {
            networkFlag = true;
        }

    }


    @Override
    public CartAdapterRecyclers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapterRecyclers.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        final Product product = productList.get(position);



        String productName ="";
        try{
            productName = product.getProductName().substring(0,1).toUpperCase() + product.getProductName().substring(1);
        }catch (Exception e){
            e.getMessage();
        }

        holder.product_name.setText(productName);
        String image = product.getThumbnailUrl();


        String imagePath = null;
        if (image != null && !image.equalsIgnoreCase("")) {
            imagePath = product.getThumbnailUrl().replaceAll(" ", "%20");
              if (networkFlag) {
            Picasso.with(context)
                    .load(imagePath).transform(transformation)
                    .placeholder(R.drawable.placeholder_lesson)   // optional
                    .error(R.drawable.placeholder_lesson)      // optional     error

                    .into(holder.imgIcon);
               } else {

                    Picasso.with(context).load(new File(imagePath)).transform(transformation).placeholder(R.drawable.placeholder_lesson)   // optional
                            .error(R.drawable.placeholder_lesson).into(holder.imgIcon);


                }
        } else {
            imagePath = "";
            holder.imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder_lesson));
        }

        holder.price_txt.setText("Price\n"+product.getproductPrice());
        holder.vendorName.setText(product.getVendorName());
        holder.vendorAddress.setText(product.getVendorAddress());

        //calling to vendor
        holder.callvendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + product.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        CartAdapterRecyclers.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onClick(View view) {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public Button callvendorBtn;
        public Button removeCartBtn;
        public ImageView imgIcon;
        public TextView product_name;
        public TextView price_txt;
        public TextView vendorName;
        public TextView vendorAddress;


        public ViewHolder(View arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub

            imgIcon = (ImageView) arg0.findViewById(R.id.product_image);
            product_name = (TextView) arg0.findViewById(R.id.product_name);
            price_txt = (TextView) arg0.findViewById(R.id.price_txt);
            vendorName = (TextView) arg0.findViewById(R.id.vendorName);
            vendorAddress = (TextView) arg0.findViewById(R.id.vendorAddress);


            callvendorBtn = (Button) arg0.findViewById(R.id.addCart);
            removeCartBtn = (Button) arg0.findViewById(R.id.removecart);

            removeCartBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            clickListener.onItemClick(getAdapterPosition(), v);

        }

    }
    public interface ClickListener {
        void onItemClick(int position, View v);

    }

}
