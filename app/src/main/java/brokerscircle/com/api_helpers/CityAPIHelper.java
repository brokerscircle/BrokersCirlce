package brokerscircle.com.api_helpers;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import brokerscircle.com.interfaces.IResult;
import brokerscircle.com.model.City.CityData;
import brokerscircle.com.model.City.CityUtil;
import brokerscircle.com.services.VolleyService;
import brokerscircle.com.util.Constant;

public class CityAPIHelper {
    private String TAG = "CityAPIHelper";
    private static final String CITY_URL = Constant.BASE_URL+"cities/list?app_id="+ Constant.APP_ID+"&app_key="+Constant.APP_KEY;

    //Volley Services
    private IResult mResultCallback = null;
    private VolleyService mVolleyService;

    private List<CityData> mCityList = new ArrayList<>();
    DataStatus dataStatus;
    Context mContext;

    public interface DataStatus{
        void DataIsLoaded(List<CityData> cityData);
    }

    public CityAPIHelper() {
    }

    public void readCitiesList(final DataStatus dataStatus, Context mContext){
        this.dataStatus = dataStatus;
this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",CITY_URL);
    }

    public void readSingleCities(final DataStatus dataStatus, Context mContext, String cityID){
        this.dataStatus = dataStatus;
this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",CITY_URL+"&id="+cityID);
    }

    public void readCitiesbyProvince(final DataStatus dataStatus, Context mContext, String provinceID){
        this.dataStatus = dataStatus;
this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",CITY_URL+"&province_id="+provinceID);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mCityList.clear();

                String plain = Html.fromHtml(response).toString();
                Log.d(TAG, "notifySuccess: "+plain);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                CityUtil[] cityUtils = gson.fromJson(plain, CityUtil[].class);

                //SETTING DATA TO DATASTATUS
                mCityList.addAll(cityUtils[0].getData());
                dataStatus.DataIsLoaded(mCityList);


            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
            }
        };
    }
}
