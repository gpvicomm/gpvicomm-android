package com.gpvicomm.payment.rest;

import com.gpvicomm.payment.rest.model.CardBinResponse;
import com.gpvicomm.payment.rest.model.CreateTokenRequest;
import com.gpvicomm.payment.rest.model.CreateTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mmucito on 13/09/17.
 */

public interface PaymentService {

    @POST("/v2/card/add")
    Call<CreateTokenResponse> createToken(@Body CreateTokenRequest createTokenRequest);

    @GET("/v2/card_bin/{bin}")
    Call<CardBinResponse> cardBin(@Path("bin") String bin);

}