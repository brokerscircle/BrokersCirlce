package brokerscirlce.com.api_helpers;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import brokerscirlce.com.util.Constant;
import brokerscirlce.com.interfaces.IResult;
import brokerscirlce.com.model.Followers.FollowersData;
import brokerscirlce.com.model.Followers.FollowersUtil;
import brokerscirlce.com.services.VolleyService;

public class FollowersDatabaseHelper {

    private String TAG = "FollowersDatabaseHelper";
    private static final String FOLLOWERS_URL = Constant.BASE_URL+"api/followers/list?app_id="+ Constant.APP_ID+"&app_key="+Constant.APP_KEY;
    //Volley services
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    DataStatus dataStatus;
    Context mContext;
    private List<FollowersData> mFollowersList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<FollowersData> usersData);
    }

    public FollowersDatabaseHelper() {
    }
    //All Followers
    public void readAllFollowers(final DataStatus dataStatus, Context mContext){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL);
    }
    //get single follow data
    public void readSingleFollowList(final DataStatus dataStatus, Context mContext, String id){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&id="+id);
    }

    //get followers
    public void readFollowedFromData(final DataStatus dataStatus, Context mContext, String id){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&followed_from_id="+id);
    }
    //get followed
    public void readFollowedToList(final DataStatus dataStatus, Context mContext, String id){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&followed_to_id="+id);
    }
    //get followers type
    public void readFollowersWithType(final DataStatus dataStatus, Context mContext, String followType){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&followed_type="+followType);
    }
    //get followers type + follower id
    //Fetch all record which contain follower id this* and type is this*  i.e type is BrokerToBroker
    public void readFollowedFromWithFollowerID_AND_Type(final DataStatus dataStatus, Context mContext, String id, String followType){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&followed_from_id="+id+"&followed_type="+followType);
    }

    //get followers type + followed id
    //Fetch all record which contain followed id this* and type is this*  i.e type is BrokerToBroker
    //Record which current user can follow someone
    public void readFollowedToWithFollowedID_AND_Type(final DataStatus dataStatus, Context mContext, String id, String followedToType){
        this.dataStatus = dataStatus;
        this.mContext = mContext;
        mFollowersList.clear();

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",FOLLOWERS_URL+"&followed_to_id="+id+"&followed_type="+followedToType);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {

                String plain = Html.fromHtml(response).toString();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                FollowersUtil[] followersUtils = gson.fromJson(plain, FollowersUtil[].class);

                //SETTING DATA TO DATASTATUS
                mFollowersList.addAll(followersUtils[0].getData());
                dataStatus.DataIsLoaded(mFollowersList);


            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
            }
        };
    }

}