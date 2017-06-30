package com.hoptec.sanket.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.hoptec.sanket.R;
import com.hoptec.sanket.database.Comment;
import com.hoptec.sanket.utl;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {
    public List<Comment> feedItemList;
    private Context ctx;

    public CommentAdapter(Context context, List<Comment> feedItemList) {
        this.feedItemList = feedItemList;
        this.ctx = context;
        AndroidNetworking.initialize(ctx);
       }

    public static int width,height;
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_row,  null, false);
      //  view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));

        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();


        Double w,h;
        w=width/1.0;
        w=w-w*0.04;
        h=height/2.4;
        CardView.LayoutParams par=new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(utl.dpFromPx(ctx,h.floatValue())<200);
      //  par=new LinearLayout.LayoutParams(w.intValue(), utl.pxFromDp(ctx,170F).intValue());


        par.bottomMargin=4;
        view.setLayoutParams(par);

//        YoYo.with(Techniques.SlideInLeft).duration(500).playOn( view);
        return viewHolder;
    }

    public static class Qant{
        public int quan=1;
    }



    @Override
    public void onBindViewHolder(final CustomViewHolder cv, final int i) {
               //Setting text view title
      final Comment cat=feedItemList.get(i);
        final int id=i;
        final Qant qn=new Qant();




  try  {

      cv.pic.setVisibility(View.GONE);


      //  customViewHolder. wb.loadData(cat.excerpt.rendered, "text/html", "UTF-8");
             utl.l("Post "+cat.user);


           //  Html.fromHtml(cat.title.rendered);
             cv.name.setText( Html.fromHtml(cat.user));
             cv.sub.setText( Html.fromHtml(cat.comment+"<br><br>"+cat.date));

      if(!cat.reply.equals("-"))
          cv.reply.setText(cat.reply);
      else
          cv.reply.setVisibility(View.GONE);


         }
         catch (Error e)
         {
          }
         catch (Exception e) {
            e.printStackTrace();
        }

        cv.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(cat,id);
            }
        });


    }

    int qan;
    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void add(Comment cat,int id)
    {

    }


    public void intr(Comment cat,int id)
    {

    }


    public void checkin(Comment cat,int id)
    {

    }

    public void disco(Comment cat,int id)
    {

    }

    public void click(Comment cat,int id)
    {

    }


public class CustomViewHolder extends RecyclerView.ViewHolder
{
    public View view;

    public ImageView pic;
    public TextView name,sub,reply;
    public WebView wb;



    public CustomViewHolder(View itemView) {
        super(itemView);
        view=itemView.findViewById(R.id.main_target);

       // wb=(WebView) itemView.findViewById(R.id.web);
        pic=(ImageView) itemView.findViewById(R.id.pic);
        name=(TextView) itemView.findViewById(R.id.text1);
        reply=(TextView) itemView.findViewById(R.id.reply);
        sub=(TextView) itemView.findViewById(R.id.plus_button);


    }
}


public class Dummy
{

}






}
