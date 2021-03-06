package com.technologies.mobile.free_exchange.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.technologies.mobile.free_exchange.Loader;
import com.technologies.mobile.free_exchange.R;
import com.technologies.mobile.free_exchange.activities.LoginActivity;
import com.technologies.mobile.free_exchange.activities.MainActivity;
import com.technologies.mobile.free_exchange.fragments.AddFragment;
import com.technologies.mobile.free_exchange.fragments.FragmentAdapter;
import com.technologies.mobile.free_exchange.rest.ExchangeClient;
import com.technologies.mobile.free_exchange.rest.RetrofitService;
import com.technologies.mobile.free_exchange.rest.VKClient;
import com.technologies.mobile.free_exchange.rest.VKRetrofitService;
import com.technologies.mobile.free_exchange.rest.model.AddResponse;
import com.technologies.mobile.free_exchange.rest.model.PersonalDataResponse;
import com.technologies.mobile.free_exchange.rest.model.VkGroupIdResponse;
import com.technologies.mobile.free_exchange.rest.model.VkPostTemplateResponse;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 02.09.2016.
 */
public class SendAsyncTask extends AsyncTask<Void, Void, Integer> {

    private static final String LOG_TAG = "mySendAsyncTask";

    private static final int GET_GROUP_ERROR = 91;
    private static final int GET_TEMPLATE_ERROR = 92;
    private static final int GET_PERSONAL_DATA_ERROR = 93;
    private static final int SUCCESS = 77;

    private AddFragment mFragment;

    private String vkGroupId;
    private String vkPostTemplate;
    private String vkId;
    private String firstName;
    private String lastName;

    private String[] images = new String[0];


    public SendAsyncTask(AddFragment fragment) {
        mFragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Loader.showSender(mFragment.getContext());
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);

        Response<VkGroupIdResponse> vkGroupIdResponse = null;
        try {
            vkGroupIdResponse = client.getVkGroupId(ExchangeClient.apiKey).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (vkGroupIdResponse == null || vkGroupIdResponse.body() == null || vkGroupIdResponse.body().getVkGroupId() == null) {
            return GET_GROUP_ERROR;
        }

        vkGroupId = vkGroupIdResponse.body().getVkGroupId().toString();

        /*Response<VkPostTemplateResponse> vkPostTemplateResponse = null;
        try {
            vkPostTemplateResponse = client.getVkPostTemplate(ExchangeClient.apiKey).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());
        }

        if (vkPostTemplateResponse == null || vkPostTemplateResponse.body() == null || vkPostTemplateResponse.body().getVkPostTemplate() == null) {
            return GET_TEMPLATE_ERROR;
        }

        vkPostTemplate = vkPostTemplateResponse.body().getVkPostTemplate();*/

        /*VKClient vkClient = VKRetrofitService.createService(VKClient.class);

        Response<PersonalDataResponse> personalDataResponse = null;
        try {
            personalDataResponse = vkClient.getUserData(VKAccessToken.currentToken().userId).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());
        }

        if (personalDataResponse == null || personalDataResponse.body() == null || personalDataResponse.body().getPersonalDataArray() == null) {
            return GET_PERSONAL_DATA_ERROR;
        }

        vkId = personalDataResponse.body().getPersonalDataArray()[0].getId();
        firstName = personalDataResponse.body().getPersonalDataArray()[0].getFirstName();
        lastName = personalDataResponse.body().getPersonalDataArray()[0].getLastName();*/

        return SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        super.onPostExecute(resultCode);
        switch (resultCode) {
            case SUCCESS: {
                Log.e(LOG_TAG, "GROUP ID = " + vkGroupId);
                uploadPhotosAndMakePost(createVkPostMessage());
                break;
            }
            case GET_GROUP_ERROR: {
                Loader.hideSender();
                Toast.makeText(mFragment.getContext(), R.string.get_group_error, Toast.LENGTH_LONG).show();
                break;
            }
            case GET_TEMPLATE_ERROR: {
                Loader.hideSender();
                Toast.makeText(mFragment.getContext(), R.string.get_template_error, Toast.LENGTH_LONG).show();
                break;
            }
            case GET_PERSONAL_DATA_ERROR: {
                Loader.hideSender();
                Toast.makeText(mFragment.getContext(), R.string.get_personal_data_error, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    public void uploadPhotosAndMakePost(final String vkMessage) {
        ArrayList<Uri> uris = mFragment.getPhotos();
        if (uris.size() != 0) {
            VKRequest[] requests = new VKRequest[uris.size()];
            for (int i = 0; i < uris.size(); i++) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mFragment.getActivity().getContentResolver(), uris.get(i));
                    requests[i] = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), 0, -1 * Integer.parseInt(vkGroupId));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }
            VKBatchRequest batchRequest = new VKBatchRequest(requests);
            batchRequest.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
                @Override
                public void onComplete(VKResponse[] responses) {
                    super.onComplete(responses);

                    Log.e(LOG_TAG, "RESPONSES!!!");
                    for (int i = 0; i < responses.length; i++) {
                        Log.e(LOG_TAG, responses[i].responseString);
                    }

                    VKApiPhoto[] photos = new VKApiPhoto[responses.length];
                    for (int i = 0; i < responses.length; i++) {
                        photos[i] = ((VKPhotoArray) responses[i].parsedModel).get(0);
                    }

                    images = new String[responses.length];
                    for (int i = 0; i < responses.length; i++) {
                        images[i] = responses[i].responseString;
                    }

                    makePost(new VKAttachments(photos), vkMessage, Integer.valueOf(vkGroupId));
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    Loader.hideSender();
                    Log.e(LOG_TAG, "uploading" + error.toString());
                }
            });
        } else {
            makePost(null, vkMessage, Integer.valueOf(vkGroupId));
        }
    }

    void makePost(VKAttachments att, String vkMessage, final int ownerId) {
        VKParameters parameters = new VKParameters();


        parameters.put(VKApiConst.OWNER_ID, ownerId);
        if (att != null) {
            parameters.put(VKApiConst.ATTACHMENTS, att);
        }
        parameters.put(VKApiConst.MESSAGE, vkMessage);

        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);

        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                // Успешно
                VKWallPostResult result = ((VKWallPostResult) response.parsedModel);
                sendToServer(result.post_id, true);
                Toast.makeText(mFragment.getContext(), R.string.vk_post_success, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                // Ошибка публикации
                Log.e(LOG_TAG, "posting" + error.toString());
                sendToServer(0, false);
                Toast.makeText(mFragment.getContext(), R.string.vk_post_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendToServer(int postId, boolean isPostIdValid) {
        ExchangeClient client = RetrofitService.createService(ExchangeClient.class);
        JSONArray JSONImagesArray;
        try {
            if(Build.VERSION.SDK_INT >= 19 ){
                JSONImagesArray = new JSONArray(images);
            }else {
                String json = "[";
                for (int i = 0; i < images.length; i++) {
                    if (i > 0) {
                        json += ",";
                    }
                    json += "\"" + images[i] + "\"";
                }
                json += "]";
                JSONImagesArray = new JSONArray(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Loader.hideSender();
            Toast.makeText(mFragment.getContext(), R.string.post_error, Toast.LENGTH_LONG).show();
            return;
        }

        String uid = PreferenceManager.getDefaultSharedPreferences(mFragment.getContext()).getString(LoginActivity.ID, null);
        if (uid == null) {
            Toast.makeText(mFragment.getContext(), R.string.post_error, Toast.LENGTH_LONG).show();
            return;
        }

        Call<AddResponse> addResponseCall;

        if (isPostIdValid) {
            addResponseCall = client.addPost(uid, postId,
                    getServerGive(), getServerGet(), getServerPM(), getPhone(), mFragment.getPlace(), JSONImagesArray, mFragment.getCategory(), ExchangeClient.apiKey);
        } else {
            addResponseCall = client.addPost(uid,
                    getServerGive(), getServerGet(), getServerPM(), getPhone(), mFragment.getPlace(), JSONImagesArray, mFragment.getCategory(), ExchangeClient.apiKey);
        }

        Log.e(LOG_TAG, "JsonImages" + JSONImagesArray.toString());

        addResponseCall.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.body().getResponse() != null) {
                    Log.e(LOG_TAG, response.body().getResponse());
                }
                if (response.body().getError() != null) {
                    Log.e(LOG_TAG, response.body().getError().getResult());
                    Log.e(LOG_TAG, response.body().getError().getCode() + "");
                }
                Loader.hideSender();
                success();
                Toast.makeText(mFragment.getContext(), R.string.successfully_completed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                Log.e(LOG_TAG, "call " + call.toString());
                Log.e(LOG_TAG, "server posting error " + t.toString());
                Loader.hideSender();
                Toast.makeText(mFragment.getContext(), R.string.post_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getServerGive() {
        String give = "";
        ArrayList<String> gives = mFragment.getGives();
        for (int i = 0; i < gives.size(); i++) {
            if (i > 0) {
                give += "\n";
            }
            give += gives.get(i);
        }
        return give;
    }

    private String getServerGet() {
        String get = "";
        ArrayList<String> gets = mFragment.getGets();
        for (int i = 0; i < gets.size(); i++) {
            if (i > 0) {
                get += "\n";
            }
            get += gets.get(i);
        }
        return get;
    }

    private String getServerPM() {
        String pm = "0";
        Map<String, Object> contactsMap = mFragment.getContacts();
        if ((boolean) contactsMap.get(AddFragment.PM)) {
            pm = "1";
        }
        return pm;
    }

    private String getPhone() {
        String phone = "";
        Map<String, Object> contactsMap = mFragment.getContacts();
        if (((String) contactsMap.get(AddFragment.PHONE)).length() != 0) {
            phone += ((String) contactsMap.get(AddFragment.PHONE));
        }
        return phone;
    }

    private String createVkPostMessage() {
        String message = "";//vkPostTemplate;

        String give = "";
        ArrayList<String> gives = mFragment.getGives();
        for (int i = 0; i < gives.size(); i++) {
            if (i > 0) {
                give += ", ";
            }
            give += gives.get(i);
        }

        String get = "";
        ArrayList<String> gets = mFragment.getGets();
        for (int i = 0; i < gets.size(); i++) {
            if (i > 0) {
                get += ", ";
            }
            get += gets.get(i);
        }

        String where = mFragment.getPlace();

        String contacts = "";
        Map<String, Object> contactsMap = mFragment.getContacts();
        if ((boolean) contactsMap.get(AddFragment.PM)) {
            contacts += "ЛС";
        }
        if (((String) contactsMap.get(AddFragment.PHONE)).length() != 0) {
            if (contacts.length() != 0) {
                contacts += ", ";
            }
            contacts += "Тел. " + ((String) contactsMap.get(AddFragment.PHONE));
        }
        if (((String) contactsMap.get(AddFragment.OTHER)).length() != 0) {
            if (contacts.length() != 0) {
                contacts += ", ";
            }
            contacts += "Другое: " + ((String) contactsMap.get(AddFragment.OTHER));
        }

        message = "Даю: " + give + "\r\n";
        message += "Получаю: " + get + "\r\n";
        message += "Место: " + where + "\r\n";
        message += "Контакты: " + contacts;

        return message;
    }

    public void success() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(mFragment.getActivity());
        fragmentAdapter.initDefaultFragment();
    }

}