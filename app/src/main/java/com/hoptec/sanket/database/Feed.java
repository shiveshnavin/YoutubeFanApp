package com.hoptec.sanket.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivesh on 14/6/17.
 */

public class Feed {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("lyrics")
    @Expose
    public Object lyrics;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("vid")
    @Expose
    public String vid;
    @SerializedName("stream_url")
    @Expose
    public String streamUrl;
    @SerializedName("download_url")
    @Expose
    public String downloadUrl;
    @SerializedName("tag_list")
    @Expose
    public Object tagList;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("user")
    @Expose
    public String user;
    @SerializedName("bpm")
    @Expose
    public Integer bpm;





}
