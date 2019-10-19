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
import brokerscircle.com.model.Units.UnitsData;
import brokerscircle.com.model.Units.UnitsUtil;
import brokerscircle.com.services.VolleyService;
import brokerscircle.com.util.Constant;

public class UnitApiHelper {

    private String TAG = "UnitApiHelper";
    private static final String URL = Constant.BASE_URL+"units/list?app_id="+ Constant.APP_ID+"&app_key="+Constant.APP_KEY;

    //Volley Services
    private IResult mResultCallback = null;
    private VolleyService mVolleyService;

    private List<UnitsData> mList = new ArrayList<>();
    DataStatus dataStatus;
    Context mContext;

    public interface DataStatus{
        void DataIsLoaded(List<UnitsData> unitsData);
    }

    public UnitApiHelper() {
    }

    public void readUnitsList(final DataStatus dataStatus, Context mContext){
        this.dataStatus = dataStatus;
        this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",URL);
    }

    public void readUnitsSingle(final DataStatus dataStatus, Context mContext, String id){
        this.dataStatus = dataStatus;
        this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",URL+"&id="+id);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mList.clear();

                String plain = Html.fromHtml(response).toString();
                Log.d(TAG, "notifySuccess: "+plain);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                UnitsUtil[] unitsUtils = gson.fromJson(plain, UnitsUtil[].class);
                //SETTING DATA TO DATASTATUS
                mList.addAll(unitsUtils[0].getData());
                dataStatus.DataIsLoaded(mList);

            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
            }
        };
    }

}