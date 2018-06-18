package com.kinitoapps.handcraftsxyz.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kinitoapps.handcraftsxyz.AppConfig;
import com.kinitoapps.handcraftsxyz.AppController;
import com.kinitoapps.handcraftsxyz.Product;
import com.kinitoapps.handcraftsxyz.R;
import com.kinitoapps.handcraftsxyz.adapters.ProductStorePageAdapter;
import com.kinitoapps.handcraftsxyz.helper.SQLiteHandler;
import com.kinitoapps.handcraftsxyz.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StorePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StorePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StorePageFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = StorePageFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean hasSubscribed=true;
    String sellerUserName;
    private SessionManager session;
    TextView storeName,storeUserName,storeSubs;
    RecyclerView recyclerView;
    Button sub_btn;
    List<Product> productList;
    private SQLiteHandler db;
    ProductStorePageAdapter adapter;

    private OnFragmentInteractionListener mListener;
    private static final String URL_STORES = "http://handicraft-com.stackstaging.com/myapi/api_all_stores.php?sto=";
    private static final String URL_PRODUCTS ="http://handicraft-com.stackstaging.com/myapi/api_all_products.php?sto=";


    public StorePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StorePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StorePageFragment newInstance(String param1, String param2) {
        StorePageFragment fragment = new StorePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sellerUserName = bundle.getString("sellerName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_store_page, container, false);
        productList = new ArrayList<>();
        storeName = root.findViewById(R.id.store_name);
        storeSubs = root.findViewById(R.id.store_subs);
        storeUserName = root.findViewById(R.id.store_username);
        sub_btn= root.findViewById(R.id.sub_button);
        db= new SQLiteHandler(getContext());
        sub_btn.setOnClickListener(this);
        session = new SessionManager(getContext());
        loadStoreInfo();
        recyclerView = root.findViewById(R.id.store_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ViewCompat.setNestedScrollingEnabled(recyclerView,false);
//        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
//            @Override
//            public boolean onFling(int velocityX, int velocityY) {
//                recyclerView.dispatchNestedFling(velocityX, velocityY, false);
//                return false;
//            }
//        });
        adapter = new ProductStorePageAdapter(getActivity(),productList);
        recyclerView.setAdapter(adapter);

        loadProducts();

        return root;
    }

    private void loadProducts() {

        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS + sellerUserName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new Product(
                                        product.getInt("id"),
                                        product.getString("primaryImage"),
                                        product.getString("productName"),
                                        product.getDouble("price"),
                                        product.getString("sellerName"),
                                        product.getString("productID")

                                ));
                            }

                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
    private void loadStoreInfo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_STORES + sellerUserName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //adding the product to product list
                                    storeName.setText(product.getString("name"));
                                    storeUserName.setText(product.getString("username"));
                                    storeSubs.setText(product.getString("subs")+" subs");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our string request to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(session.isLoggedIn())
            checkSub();
    }

    @Override
    public void onClick(View view) {
        if(session.isLoggedIn()) {
            if (view == sub_btn) {
                if (!hasSubscribed)
                    getSubscribed();
                else
                    getUnsubscribed();
            }
        }
        else{
            Toast.makeText(getActivity(), "Please Sign in to Subscribe!", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkSub(){
        final String subscriber_uid=db.getUserDetails().get("email"),unique_store_id= sellerUserName;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CHECKSUB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        try {
                            JSONObject obj = new JSONObject(ServerResponse);
                            if(obj.getString("subscribed").equals("TRUE")) {
                                hasSubscribed = true;
                                sub_btn.setText("UNSUBSCRIBE");
                            }
                            else {
                                hasSubscribed = false;
                                sub_btn.setText("SUBSCRIBE");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Hiding the progress dialog after all task complete.

                        // Showing response message coming from server.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.

                        // Showing error message if something goes wrong.
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()  {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("subscriber_uid", subscriber_uid);
                params.put("unique_store_id", unique_store_id);
                Log.v("subs1",subscriber_uid);
                Log.v("sub2",unique_store_id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void getUnsubscribed() {
        final String subscriber_uid=db.getUserDetails().get("email"),unique_store_id= sellerUserName;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UNSUBSCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        sub_btn.setText("SUBSCRIBE");
                        hasSubscribed = false;
                        // Hiding the progress dialog after all task complete.

                        // Showing response message coming from server.
                        Toast.makeText(getContext(), "Unsubscribed Successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.

                        // Showing error message if something goes wrong.
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()  {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("subscriber_uid", subscriber_uid);
                params.put("unique_store_id", unique_store_id);
                Log.v("subs1",subscriber_uid);
                Log.v("sub2",unique_store_id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void getSubscribed() {
        if (session.isLoggedIn()){
            String subscriber_uid=db.getUserDetails().get("email"),unique_store_id= sellerUserName;
            userSubscription(subscriber_uid,unique_store_id);
        }

    }
    private void userSubscription( final String subscriber_uid, final String unique_store_id){

        // another try
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SUBSCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        sub_btn.setText("UNSUBSCRIBE");
                        hasSubscribed = true;
                        // Hiding the progress dialog after all task complete.
                        Toast.makeText(getActivity(), "Subscribed Successfully!", Toast.LENGTH_SHORT).show();
                        // Showing response message coming from server.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.

                        // Showing error message if something goes wrong.
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()  {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("subscriber_uid", subscriber_uid);
                params.put("unique_store_id", unique_store_id);
                Log.v("subs1",subscriber_uid);
                Log.v("sub2",unique_store_id);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
