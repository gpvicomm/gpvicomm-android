package com.gpvicomm.payment.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gpvicomm.payment.model.Card;

/**
 * Utility class for functions to do with cards.
 */
public class CardUtils {
    private static final int LENGTH_COMMON_CARD = 16;
    private static final int LENGTH_AMERICAN_EXPRESS = 15;
    private static final int LENGTH_DINERS_CLUB = 14;

    private static String CARD_BRAND = null;

    /**
     * Returns a {@link Card.CardBrand} corresponding to a partial card number,
     * or {@link Card#UNKNOWN} if the card brand can't be determined from the input value.
     *
     * @param cardNumber a credit card number or partial card number
     * @return the {@link Card.CardBrand} corresponding to that number,
     * or {@link Card#UNKNOWN} if it can't be determined
     */
    @NonNull
    @Card.CardBrand
    public static String getPossibleCardType(@Nullable String cardNumber) {
        return getPossibleCardType(cardNumber, true);
    }

    /**
     * Checks the input string to see whether or not it is a valid card number, possibly
     * with groupings separated by spaces or hyphens.
     *
     * @param cardNumber a String that may or may not represent a valid card number
     * @return {@code true} if and only if the input value is a valid card number
     */
    public static boolean isValidCardNumber(@Nullable String cardNumber) {
        String normalizedNumber = PaymentTextUtils.removeSpacesAndHyphens(cardNumber);
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(normalizedNumber);
    }

    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return {@code true} if and only if the input value is a valid Luhn number
     */
    static boolean isValidLuhnNumber(@Nullable String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        boolean isOdd = true;
        int sum = 0;

        for (int index = cardNumber.length() - 1; index >= 0; index--) {
            char c = cardNumber.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }

            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }

    /**
     * Checks to see whether the input number is of the correct length, after determining its brand.
     * This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return {@code true} if the card number is of known type and the correct length
     */
    static boolean isValidCardLength(@Nullable String cardNumber) {
        return cardNumber != null && isValidCardLength(cardNumber,
                getPossibleCardType(cardNumber, false));
    }

    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @param cardBrand a used to get the correct size
     * @return {@code true} if the card number is the correct length for the assumed brand
     */
    static boolean isValidCardLength(
            @Nullable String cardNumber,
            @NonNull @Card.CardBrand String cardBrand) {
        if(cardNumber == null || Card.UNKNOWN.equals(cardBrand)) {
            return false;
        }

        int length = cardNumber.length();
        switch (cardBrand) {
            case Card.AMERICAN_EXPRESS:
                return length == LENGTH_AMERICAN_EXPRESS;
            case Card.DINERS_CLUB:
                return length == LENGTH_DINERS_CLUB;
            default:
                return length == LENGTH_COMMON_CARD;
        }
    }

    public static void setPossibleCardType(String card_brand){
        CARD_BRAND = card_brand;
    }

    @NonNull
    private static String getPossibleCardType(@Nullable String cardNumber,
                                              boolean shouldNormalize) {
        if(CARD_BRAND != null)
            return CARD_BRAND;

        if (PaymentTextUtils.isBlank(cardNumber)) {
            return Card.UNKNOWN;
        }

        String spacelessCardNumber = cardNumber;
        if (shouldNormalize) {
            spacelessCardNumber = PaymentTextUtils.removeSpacesAndHyphens(cardNumber);
        }




        if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_AMERICAN_EXPRESS)) {
            return Card.AMERICAN_EXPRESS;
        } else if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_DISCOVER)) {
            return Card.DISCOVER;
        } else if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_JCB)) {
            return Card.JCB;
        } else if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_DINERS_CLUB)) {
            return Card.DINERS_CLUB;
        } else if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_VISA)) {
            return Card.VISA;
        } else if (PaymentTextUtils.hasAnyPrefix(spacelessCardNumber, Card.PREFIXES_MASTERCARD)) {
            return Card.MASTERCARD;
        } else {
            return Card.UNKNOWN;
        }
    }
}

