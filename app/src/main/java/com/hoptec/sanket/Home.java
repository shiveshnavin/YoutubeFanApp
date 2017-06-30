package com.hoptec.sanket;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.hoptec.sanket.adapters.PosterAdapter;
import com.hoptec.sanket.database.Feed;
import com.hoptec.sanket.database.OUser;
import com.hoptec.sanket.utils.FileOperations;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    public   Context ctx;
    public   Activity act;

    private List<String> lastSearches;
    private MaterialSearchBar searchBar;

    ArrayList<Feed> feeds;
    Gson js;

    @BindView(R.id.tmp)
    TextView tmp;

    @BindView(R.id.rec)
    RecyclerView rec;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;

        utl.init(getApplication());
        utl.fullScreen(act);
        setContentView(R.layout.activity_home);

        checkPermission();

        js=new Gson();
        ButterKnife.bind(this);

        user = utl.getUser();

        FileOperations fop=new FileOperations();

        setUpToolbar();

//        utl.snack(act,"Welcome Back ! "+utl.readData(ctx).name);
        //startlogin(user.email,user.suserId,0);




    }

    public void checkPermission()
    {
        String permissionss []={Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.GET_ACCOUNTS
        };
        ActivityCompat.requestPermissions(act,
                permissionss,
                1);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.



                } else {

                    Toast.makeText(ctx, "Please Grant Permissions for app to work properly !", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onResume()
    {

        AndroidNetworking.initialize(ctx);
        String url=Constants.HOST+"/api/getcontent.php";
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

                utl.l(response);
                feeds=new ArrayList<>();
                try {
                    JSONArray jr=new JSONArray(response);
                    for(int i=0;i<jr.length();i++)
                    {

                        feeds.add(js.fromJson(jr.get(i).toString(),Feed.class));
                    }


                    setUpList(feeds);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError ANError) {

            }
        });
        super.onResume();;
    }
    public void setUpToolbar()
    {



        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search");
        searchBar.setNavButtonEnabled(false);


        searchBar.setSpeechMode(false);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {
                String s = enabled ? "enabled" : "disabled";
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                utl.hideSoftKeyboard(act);
                // utl.snack(act,text.toString());



                String url=Constants.HOST+"/api/getcontent.php?search="+ URLEncoder.encode(text.toString());
                AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        utl.l(response);
                        feeds=new ArrayList<>();
                        try {
                            JSONArray jr=new JSONArray(response);
                            for(int i=0;i<jr.length();i++)
                            {

                                feeds.add(js.fromJson(jr.get(i).toString(),Feed.class));
                            }


                            setUpList(feeds);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError ANError) {

                    }
                });



            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                }
            }

        });
        //restore last queries from disk
        lastSearches = loadSearchSuggestion();
        searchBar.setLastSuggestions(lastSearches);

        searchBar.inflateMenu(R.menu.main);
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.logout)
                {
                    utl.l("LOGGING OUT AFTER MENU CLICK");
                    utl.logout();
                    startActivity(new Intent(ctx, Splash.class));
                    finish();
                }
                return false;
            }
        });

    }


    public List<String> loadSearchSuggestion(){


        List<String> list=new ArrayList<>();
        return list;
    }

    public void setUpList(ArrayList<Feed> feeds)
    {




        PosterAdapter adapter=new PosterAdapter(ctx,feeds){

            @Override
            public void click(Feed cat, int id) {
                super.click(cat, id);

                utl.logEvent("Clicked : "+cat.title,null);

                Intent ir=new Intent(ctx,Detail.class);
                ir.putExtra("cat",js.toJson(cat));
                startActivity(ir);
//                utl.snack(act,cat.title);





            }
        };


        rec=(RecyclerView)findViewById(R.id.rec);

        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);













    }

}