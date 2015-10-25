package com.pledgeapp.pledge;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pledgeapp.pledge.models.NonProfit;

import io.card.payment.CardType;
import io.card.payment.CreditCard;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * 
 */
public class PledgeClient {
	public static final String PLEDGE_SERVICE_BASE_URL = "http://146.148.84.121";

    private AsyncHttpClient mClient;

    private static PledgeClient sInstance;
    public static synchronized PledgeClient getInstance() {
        if (sInstance == null) {
            sInstance = new PledgeClient();
        }
        return sInstance;
    }

	private PledgeClient() {
        // TODO(ageiduschek): Authenticate requests with Google Oauth token.
        mClient = new AsyncHttpClient();
	}

	public void getFeatured(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/featured");

		RequestParams params = new RequestParams();

		mClient.get(apiUrl, params, handler);
	}

	public void getLocal(AsyncHttpResponseHandler handler) {
		// TODO(nikhilb): Update this endpoint when you actually build "/local"

		String apiUrl = getApiUrl("/featured");

		RequestParams params = new RequestParams();

        mClient.get(apiUrl, params, handler);
	}

    public void getCreditCards(String userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/fetch_credit_cards");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);

        mClient.get(apiUrl, params, handler);
    }

    public void addCreditCard(String email, CreditCard creditCard, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/store_credit_card");

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("cc_number", creditCard.cardNumber);
        params.put("cvv", creditCard.cvv);
        params.put("expiry_year", creditCard.expiryYear);
        params.put("expiry_month", creditCard.expiryMonth);
        String cardType = "Unknown";
        if (creditCard.getCardType() == CardType.AMEX) {
            cardType = "Amex";
        } else if (creditCard.getCardType() == CardType.VISA) {
            cardType = "Visa";
        } else if (creditCard.getCardType() == CardType.MASTERCARD) {
            cardType = "Mastercard";
        }
        params.put("card_type", cardType);

        mClient.post(apiUrl, params, handler);
    }

    public void donate(String userId, String cardId, double amount, NonProfit nonProfit, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/donate");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("card_id", cardId);
        params.put("amount", amount);
        params.put("ein", nonProfit.getEin());

        mClient.post(apiUrl, params, handler);
    }

    public void getDonationHistory(String userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/fetch_donation_history");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);

        mClient.get(apiUrl, params, handler);
    }

	public void search(String query, NonProfit.CategoryInfo category, int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/search");

        RequestParams params = new RequestParams();
        params.put("q", query);
		if (category != null) {
			params.put("category", category.searchIndex);
		}
        params.put("page", page);

        mClient.get(apiUrl, params, handler);
	}

    protected String getApiUrl(String path) {
        return PLEDGE_SERVICE_BASE_URL + path;
    }

    public void createOrFindUser(String email,
                                 String firstName,
                                 String lastName,
                                 AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/create_or_find_user");

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("first_name", firstName);
        params.put("last_name", lastName);

        mClient.post(apiUrl, params, handler);
    }


    public void getMajorCategories(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/major_categories");
        mClient.get(apiUrl, handler);
    }

    public void getSubcategories(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/sub_categories");
        mClient.get(apiUrl, handler);
    }
}