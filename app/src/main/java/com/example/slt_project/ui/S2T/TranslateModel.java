package com.example.slt_project.ui.S2T;

import android.os.AsyncTask;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.nlp.v2.NlpClient;
import com.huaweicloud.sdk.nlp.v2.model.RunTextTranslationRequest;
import com.huaweicloud.sdk.nlp.v2.model.RunTextTranslationResponse;
import com.huaweicloud.sdk.nlp.v2.model.TextTranslationReq;
import com.huaweicloud.sdk.nlp.v2.region.NlpRegion;

import java.util.Collections;

public class TranslateModel extends AsyncTask<RunTextTranslationRequest, Void, String> {
    S2TContract.IS2TModel model;
    TranslateModel(S2TContract.IS2TModel model){
        this.model=model;
    }


    @Override
    protected String doInBackground(RunTextTranslationRequest... req2) {
        RunTextTranslationResponse response = client.runTextTranslation(req2[0]);
        return response.getTranslatedText();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        model.getPresenter().getFragment().getAdapter().addText(s);
        model.getPresenter().speak(s);
    }

    private static ICredential auth = new BasicCredentials()
            .withAk("PBPXJMQH6PC8X9BOV5KI")
            .withSk("8BjR2JDq4DEVEKTpnLXENrLDadhiHIeUXjAZKB9D")
            .withProjectId("db2c5bc7cffb4b07a4650260d6051cf9");


    private static NlpClient client = NlpClient.newBuilder().withCredential(auth)
            .withRegion(NlpRegion.valueOf("cn-north-4"))
            .withEndpoints(Collections.singletonList("nlp-ext.cn-north-4.myhuaweicloud.com"))
            .build();



}