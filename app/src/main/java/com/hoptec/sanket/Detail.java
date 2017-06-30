package com.hoptec.sanket;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import com.hoptec.sanket.adapters.CommentAdapter;
import com.hoptec.sanket.database.Comment;
import com.hoptec.sanket.database.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;


public class Detail extends YouTubeBaseActivity {
    YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    public static Context ctx;
    public static Activity act;



     @BindView(R.id.rec)
    RecyclerView rec;

     @BindView(R.id.title)
    TextView title;


     @BindView(R.id.desc)
    TextView desc;

    ArrayList<Comment> comments;


     @BindView(R.id.scrl)
    NestedScrollView scrl;


     @BindView(R.id.like)
    ImageView like;

     @BindView(R.id.comment)
    ImageView comment;

     @BindView(R.id.share)
    ImageView share;


    Gson js;
    Feed fed;
    YouTubePlayer.OnInitializedListener opt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        act=this;
        utl.fullScreen(act);

        setContentView(R.layout.content_detail);

        ButterKnife.bind(this);
        js=new Gson();


        String jst=getIntent().getStringExtra("cat");
        fed=js.fromJson(jst,Feed.class);
        set();

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   utl.toast(ctx,"Liked");
                like();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });

        opt= new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.cueVideo(fed.vid);
                    player.play();
                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {

                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onStopped() {

                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {

                        }
                    });

                    // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(act, RECOVERY_REQUEST).show();
                } else {
                    String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
                    Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
                }
            }
        };


        if(utl.isLiked(fed.vid))
            like.setImageResource(R.drawable.loved);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        try {
            youTubeView.initialize(Constants.YOUTUBE_API_KEY,opt);
        } catch (Exception e) {
            e.printStackTrace();
        }



        getCOmments();
        rec.setNestedScrollingEnabled(false);

        scrl.postDelayed(new Runnable() {
            @Override
            public void run() {

             //   scrl.smoothScrollTo(0,0);
            }
        },500);





    }


    public void comment(final String comment)
    {

        String url=Constants.HOST+"/api/comment.php?user="+URLEncoder.encode(utl.getUser().getDisplayName())+"&comment="+ URLEncoder.encode(comment)+"&vid="+fed.vid;
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                utl.l(response);

                    utl.logEvent("Comment Success ",comment);


                utl.snack(scrl,"Comment Posted !");


                getCOmments();


            }

            @Override
            public void onError(ANError ANError) {

            }
        });

    }





    public void getCOmments()
    {

        String url=Constants.HOST+"/api/getcomments.php?vid="+fed.vid;
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                utl.l(response);
                comments=new ArrayList<>();
                try {
                    JSONArray jr=new JSONArray(response);
                    for(int i=0;i<jr.length();i++)
                    {

                        comments.add(js.fromJson(jr.get(i).toString(),Comment.class));
                    }


                    setUpList(comments);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError ANError) {

            }
        });

    }

    public void set()
    {

        title.setText(fed.title);
        desc.setText(fed.description+"\n\n"+fed.createdAt+"\n\n"+fed.bpm+" Likes");

    }

    public boolean check()
    {

        boolean loggedin=true;


        if(utl.getUser()==null)
        {
            loggedin=false;
        }

        return loggedin;

    }

    public void login(final String doi)
    {


        utl.logEvent("Tried "+doi+" but need login() ",fed.vid);
        utl.logEvent("USER DATA IS ", ""+utl.getUser());




        startActivity(new Intent(Detail.this, Splash.class));
        finish();
        if(true)
            return;
        View rootView = act.getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(rootView,  "Must Login First !", Snackbar.LENGTH_LONG);
        snackbar.setAction("LOGIN", new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        snackbar.setActionTextColor(act.getResources().getColor(R.color.material_red_A400));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));


        snackbar.setActionTextColor(act.getResources().getColor(R.color.white));

         int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(Color.WHITE);


    }


    public void like()
    {


        if(!check())
        {



            login("like");
            return;
        }
         if(utl.isLiked(fed.vid))
            fed.bpm--;
        else
            fed.bpm++;
        set();
        like.setImageResource(R.drawable.loved);


        String url=Constants.HOST+"/api/like.php?vid="+fed.vid;
        if(utl.isLiked(fed.vid))
        {
            like.setImageResource(R.drawable.love);
            url=Constants.HOST+"/api/unlike.php?vid="+fed.vid;
            utl.unLike(fed.vid);
        }
        else{
            utl.setLiked(fed.vid);
        }
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                utl.l(response);

                utl.logEvent("Liked Success ",response);

                utl.snack(findViewById(R.id.hold),""+response);





            }

            @Override
            public void onError(ANError ANError) {

            }
        });



    }


    AlertDialog dig;
    public void comment()
    {

        if(!check())
        {
            login("comment");
            return;
        }

        utl.inputDialog(ctx, "Comment", "Say something...", utl.TYPE_DEF, new utl.InputDialogCallback() {
            @Override
            public void onDone(String text) {
                comment(text);
            }
        });



    }



    public void share()
    {



        Intent txtIntent = new Intent(Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(Intent.EXTRA_SUBJECT, fed.title);
        txtIntent .putExtra(Intent.EXTRA_TEXT, fed.streamUrl);
        startActivity(Intent.createChooser(txtIntent ,"Share Media"));
    }




    public void setUpList(ArrayList<Comment> feeds)
    {




        CommentAdapter adapter=new CommentAdapter(ctx,feeds){

            @Override
            public void click(Comment cat, int id) {
                super.click(cat, id);




            }
        };


        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);





/*
        try {
            scrl.smoothScrollTo(0,rec.getTop()+100);
            rec.scrollToPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scrl.smoothScrollTo(0,rec.getBottom());
        scrl.smoothScrollTo(0,scrl.getBottom());
        try {
            rec.scrollToPosition(adapter.getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.YOUTUBE_API_KEY, opt);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
