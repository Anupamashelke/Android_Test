package app.test.com.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import app.test.com.myapplication.R;
import app.test.com.myapplication.adapter.CartAdapterRecyclers;
import app.test.com.myapplication.database.ProductDatabaseHelper;
import app.test.com.myapplication.model.Product;

/**
 * Created by anupama.shelke on 9/29/2016.
 */
public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductDatabaseHelper db;
    List<Product> CartList = new ArrayList<>();
    private CartAdapterRecyclers mAdapter;
    private TextView totalpriceView;
    private Toolbar toolbar_bottom;
    private TextView errorTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_cart,
                container, false);
        recyclerView = (RecyclerView) viewMain.findViewById(R.id.cart_recycler_view);
        toolbar_bottom = (Toolbar) viewMain.findViewById(R.id.toolbar_bottom);
        totalpriceView = (TextView) viewMain.findViewById(R.id.totalpriceView);
        errorTextView = (TextView) viewMain.findViewById(R.id.errorView);

        //created database object to deal with local data.
        db = new ProductDatabaseHelper(getActivity().getApplicationContext());

        prepareOfflineData();
        //handle the click of remove product cart
        mAdapter.setOnItemClickListener(new CartAdapterRecyclers.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //    Toast.makeText(getActivity(),"onItemClick position: "+ position,Toast.LENGTH_SHORT).show();
                int productid = CartList.get(position).getProductId();
                db.deletePerticularProduct(String.valueOf(productid));

                prepareOfflineData();
            }

        });
        return viewMain;
    }

    private void prepareOfflineData() {
        CartList.clear();
        CartList = db.getAllProducts();

        int totalofProductPrice = db.getAllProductsPrice();
        //show the product price on the cart view
        if (totalofProductPrice == 0) {
            toolbar_bottom.setVisibility(View.GONE);
        } else {
            toolbar_bottom.setVisibility(View.VISIBLE);
            totalpriceView.setText("Total Price: " + totalofProductPrice);
        }


        mAdapter = new CartAdapterRecyclers(getActivity().getApplicationContext(), CartList);
        if (CartList.size() != 0) {
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        } else {

            errorTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareOfflineData();

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            prepareOfflineData();

        }
    }

}
