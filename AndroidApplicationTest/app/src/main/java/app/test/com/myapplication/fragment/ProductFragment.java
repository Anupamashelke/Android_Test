package app.test.com.myapplication.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.test.com.myapplication.R;
import app.test.com.myapplication.Utilities.CheckNetworkConnection;
import app.test.com.myapplication.adapter.ProductAdapterRecyclers;
import app.test.com.myapplication.model.Product;
import app.test.com.myapplication.webservice.WebServiceVolley;
import app.test.com.myapplication.webservice.configConstants;

/**
 * Created by  anupama.shelke on 9/29/2016.
 */
public class ProductFragment extends android.support.v4.app.Fragment {

    private ProgressDialog asyncDialog;
    private JSONArray productDataArray;
    private List<Product> ProductList = new ArrayList<Product>();
    private RecyclerView recyclerView;
    private ProductAdapterRecyclers mAdapter;
    private TextView errorTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_product,
                container, false);
        recyclerView = (RecyclerView)viewMain.findViewById(R.id.recycler_view);
        errorTextView=(TextView)viewMain.findViewById(R.id.errorView);


        if (CheckNetworkConnection.isConnectionAvailable(getActivity().getApplicationContext())) {
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            prepareProductUrl();
        } else {

            Toast.makeText(getActivity(),"Wait! Can\'t connect to internet!!",Toast.LENGTH_SHORT).show();
        }

        return viewMain;
    }

    @Override
    public void onResume() {
        super.onResume();
       // prepareLoginUrl();
    }

    private void prepareProductUrl() {
        String URL_FEED_UPDATE = configConstants.APP_BASEURL ;
        //  ArrayList<String> key = new ArrayList<>();





        WebServiceVolley webServiceVolley = new WebServiceVolley(getActivity().getApplicationContext(), callback);
        webServiceVolley.getProductDetailsApi(getActivity().getApplicationContext(),URL_FEED_UPDATE);
        asyncDialog = new ProgressDialog(getActivity());
        asyncDialog.setMessage("Logging please wait...");
        asyncDialog.setCanceledOnTouchOutside(false);
        //show dialog
        asyncDialog.show();
    }


    WebServiceVolley.Callback callback = new WebServiceVolley.Callback() {
        @Override
        public void onSuccessJsonObject(int reqestcode, JSONObject jsonResp) {

            asyncDialog.dismiss();

            //parse the data in  following method

            parseLessonJsonData(jsonResp);
        }



        @Override
        public void onError(int reqestcode, String error) {
            asyncDialog.dismiss();

            if (reqestcode == 20||reqestcode == 21||reqestcode == 22) {
                Toast.makeText(getActivity(),"Wait! Can\'t connect to internet!!",Toast.LENGTH_SHORT).show();
            } else if (reqestcode == 23) {
                Toast.makeText(getActivity(),"Authentication Error!",Toast.LENGTH_SHORT).show();
            } else if (reqestcode == 24) {
                Toast.makeText(getActivity(),"Parse Error!",Toast.LENGTH_SHORT).show();
            } else if (reqestcode == 25) {
                Toast.makeText(getActivity(),"can't connect to server",Toast.LENGTH_SHORT).show();
            }

        }


    };
    private void parseLessonJsonData(JSONObject jsonResp) {

        ProductList.clear();

        try {
              productDataArray = jsonResp.getJSONArray("products");
            /*
            check the data avaliable or not
            if avaliable shown the list
            else No data found.
             */


        for (int count = 0; count < productDataArray.length(); count++) {
        JSONObject ProductItem = productDataArray.getJSONObject(count);

        Product product = new Product();

        product.setProductName(ProductItem.getString("productname"));
        product.setVendorName(ProductItem.getString("vendorname"));
        product.setVendorAddress(ProductItem.getString("vendoraddress"));
        product.setproductPrice(ProductItem.getInt("price"));
        String imagePath = ProductItem.isNull("productImg") ? null
                : ProductItem.getString("productImg");
        product.setPhoneNumber(ProductItem.getString("phoneNumber"));
        product.setThumbnailUrl(imagePath);

        ProductList.add(product);

    }
            if(ProductList.size()!=0) {
    mAdapter = new ProductAdapterRecyclers(getActivity().getApplicationContext(), ProductList);

    final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);
}else
{
    errorTextView.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);

}
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
