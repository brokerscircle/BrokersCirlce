package brokerscircle.com.api_helpers.Property;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import brokerscircle.com.interfaces.IResult;
import brokerscircle.com.model.Property.Property_Child_Category.PropertyChildCategoryUtils;
import brokerscircle.com.services.VolleyService;
import brokerscircle.com.util.Constant;

public class PropertyChildCategoryApiHelper {

    private String TAG = "PropertyChildCategoryApiHelper";
    private static final String URL = Constant.BASE_URL+"property-child-category/list?app_id="+ Constant.APP_ID+"&app_key="+Constant.APP_KEY;

    //Volley Services
    private IResult mResultCallback = null;
    private VolleyService mVolleyService;


    private List<PropertyChildCategoryUtils> mChildList = new ArrayList<>();
    DataStatus dataStatus;
    Context mContext;

    public interface DataStatus{
        void DataIsLoaded(List<PropertyChildCategoryUtils> propertyChildCategoryUtils);
    }

    public PropertyChildCategoryApiHelper() {
    }

    public void readPropertyChildCatergoryList(final DataStatus dataStatus, Context mContext){
        this.dataStatus = dataStatus;
        this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",URL);
    }

    public void readSinglePropertyChildCatergory(final DataStatus dataStatus, Context mContext, String id){
        this.dataStatus = dataStatus;
        this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",URL+"&id="+id);
    }

    public void readPropertyChildCatergoryByParentCatergory(final DataStatus dataStatus, Context mContext, String parentID){
        this.dataStatus = dataStatus;
        this.mContext = mContext;

        //Getting Json from url
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,mContext);
        mVolleyService.getDataVolley("GETCALL",URL+"&property_parent_category_id=="+parentID);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {

            @Override
            public void notifySuccess(String requestType,String response) {
                mChildList.clear();
                String plain = Html.fromHtml(response).toString();
                Log.d(TAG, "notifySuccess: "+plain);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                PropertyChildCategoryUtils[] propertyChildCategoryUtils = gson.fromJson(plain, PropertyChildCategoryUtils[].class);

                //SETTING DATA TO DATASTATUS
                mChildList.add(propertyChildCategoryUtils[0]);
                dataStatus.DataIsLoaded(mChildList);


            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
            }
        };
    }

}
