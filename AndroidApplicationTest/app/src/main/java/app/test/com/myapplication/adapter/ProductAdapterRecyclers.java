package app.test.com.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by anupama.shelke on 9/28/2016.
 * This Adapter used for showing the product list on product tab
 */
public class ProductAdapterRecyclers extends RecyclerView.Adapter<ProductAdapterRecyclers.ViewHolder> {

    private final Context context;
    private final List<Product> productList;
    final int radius = 10;
    final int margin = 0;
    private boolean networkFlag = false;
    final Transformation transformation = new RoundedCornersTransformation(radius, margin, RoundedCornersTransformation.CornerType.ALL);
    private ProductDatabaseHelper db;
    public ProductAdapterRecyclers(Context activity, List<Product> List) {
        this.context = activity;
        this.productList = List;
        db = new ProductDatabaseHelper(context);
        if (CheckNetworkConnection.isConnectionAvailable(context.getApplicationContext())) {
            networkFlag = true;
        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.setIsRecyclable(false);

        final Product product = productList.get(position);



            String upperString ="";
            try{
                upperString = product.getProductName().substring(0,1).toUpperCase() + product.getProductName().substring(1);
            }catch (Exception e){
                e.getMessage();
            }

            holder.product_name.setText(upperString);
            String image = product.getThumbnailUrl();

            System.out.println("image name adapter==" + image);
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

        holder.price_txt.setText("Price: "+product.getproductPrice());
        holder.vendorName.setText(product.getVendorName());
        holder.vendorAddress.setText(product.getVendorAddress());
        holder.addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                /*insert the the records in Database on Add to cart click */
                db.insertProductRecordIntoDatabase(product);
            }
        });
    }




    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public Button addCartBtn;

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


            addCartBtn = (Button) arg0.findViewById(R.id.addCart);


        }


        @Override
        public boolean onLongClick(View v) {
            // TODO Auto-generated method stub
            //itemClickListener.onClick(v, getPosition(), true);
            return true;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            //itemClickListener.onClick(v, getPosition(), false);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.product_list_item, arg0, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


}
