package com.example.youtubeapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeapp.R;
import com.example.youtubeapp.activitys.MainActivity;
import com.example.youtubeapp.adapter.HintAdapter;
import com.example.youtubeapp.api.ApiServiceHintSearch;
import com.example.youtubeapp.my_interface.IItemOnClickHintListener;
import com.example.youtubeapp.my_interface.IItemOnClickSearchListener;
import com.example.youtubeapp.utiliti.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchFragment extends Fragment {
    private ImageButton ibBack;
    String how;
    EditText etSearch;
    RecyclerView rvListHint;
    ArrayList<String> listHint;
    HintAdapter adapter;
    MainActivity mainActivity;
    Toolbar tbSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        backHome();
        getBundle();
        etSearch.setText(how);
        etSearch.requestFocus();
//        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mainActivity = (MainActivity) getActivity();
        listHint = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvListHint.setLayoutManager(linearLayoutManager);
        adapter = new HintAdapter(new IItemOnClickHintListener() {
            @Override
            public void onClickListener(String s) {
                etSearch.setText(s);
            }
        }, // khi click tìm kiếm
                new IItemOnClickSearchListener() {
            @Override
            public void onClickSearchListener(String q) {
//                removeSearchFragment();
                mainActivity.addFragmentSearchResults(q);
                etSearch.setText(q);
            }
        });
        rvListHint.setAdapter(adapter);

        adapter.setData(listHint);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = etSearch.getText().toString();
                tbSearch.getMenu().findItem(R.id.mn_voice_search).setVisible(false);
                tbSearch.getMenu().findItem(R.id.mn_close_search).setVisible(true);
                callApiHintSearch(ss);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = etSearch.getText().toString();
                if (ss.equals("")) {
                    tbSearch.getMenu().findItem(R.id.mn_voice_search).setVisible(true);
                    tbSearch.getMenu().findItem(R.id.mn_close_search).setVisible(false);
                    listHint.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s = etSearch.getText().toString();
                    mainActivity.addFragmentSearchResults(s);
                    etSearch.setText(s);
                    return true;
                }
                return false;
            }
        });

//        callApiHintSearch("thích");

        // sự kiện nút xóa edittext
        tbSearch.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_search:
                        etSearch.setText("");
                }
                return false;
            }
        });
        return view;
    }

    private void initView(View view) {
        ibBack = view.findViewById(R.id.ib_back_search);
        etSearch = view.findViewById(R.id.et_search);
        rvListHint = view.findViewById(R.id.rv_list_hint_search);
        tbSearch = view.findViewById(R.id.tb_nav_search);
    }
    // Quay trở lại trước đó
    private void backHome() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

//    public void removeSearchFragment() {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.remove(this);
//            transaction.commit();
//        getChildFragmentManager().popBackStack();
//    }



    private void callApiHintSearch(String hint) {
        listHint.clear();
        String url = "http://suggestqueries.google.com/complete/search?client=youtube&ds=yt&client=firefox&q="  + hint + "";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("duc123", response.toString());
                try {
                    Object string = response.get(1);
                    Log.d("duc11", string.toString());
                    String s = string.toString();
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String string1 = (String) jsonArray.get(i);
                        listHint.add(string1);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getBundle() {
        Bundle bundleRe = getArguments();
        how = bundleRe.getString(Util.BUNDLE_EXTRA_TEXT_EDITTEXT);
    }
}