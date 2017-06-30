
package com.hoptec.sanket.database;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hoptec.sanket.GenricUser;


public class OUser {


    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_fname")
    @Expose
    public String userFname;
    @SerializedName("user_password")
    @Expose
    public String userPassword;
    @SerializedName("auth")
    @Expose
    public String auth;
    @SerializedName("user_email")
    @Expose
    public String userEmail;
    @SerializedName("user_image")
    @Expose
    public String userImage;
    @SerializedName("user_created")
    @Expose
    public String userCreated;
    @SerializedName("user_role")
    @Expose
    public String userRole;
    @SerializedName("user_group")
    @Expose
    public String userGroup;
    @SerializedName("user_status")
    @Expose
    public String userStatus;
    @SerializedName("user_id")
    @Expose
    public String userId;



    public GenricUser toGenricUser(String password)
    {
        GenricUser user=new GenricUser();

        user.user_name=this.userName;
        user.user_fname=this.userFname;
        user.user_email=this.userEmail;
        user.user_image=this.userImage;;
        user.user_password=password;
        user.uid=this.userId;


        user.user_created=this.userCreated;

        user.suid=this.auth;

        return user;
    }


    public GenricUser toGenricUser()
    {
        GenricUser user=new GenricUser();

        user.user_name=this.userName;
        user.user_fname=this.userFname;
        user.user_email=this.userEmail;
        user.user_image=this.userImage;;
        user.uid=userId;
        user.uid=this.userId;


        user.user_created=this.userCreated;

        user.suid=this.auth;
        return user;
    }




}
