package com.gpvicomm.payment.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gpvicomm.payment.model.Card;

public class CreateTokenResponse{

    @SerializedName("card")
    @Expose
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
