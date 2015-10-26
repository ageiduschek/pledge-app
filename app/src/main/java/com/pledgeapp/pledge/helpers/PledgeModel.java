package com.pledgeapp.pledge.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeClient;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.Donation;
import com.pledgeapp.pledge.models.NonProfit;
import com.pledgeapp.pledge.models.PledgeCard;
import com.pledgeapp.pledge.models.User;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CreditCard;

/**
 * Created by ageiduschek on 10/23/15.
 */
public class PledgeModel {
    public static class OnResultDelegate<T> {
        private ProgressDialog pd;
        private Context mContext;
        private boolean mShowSpinner;

        public OnResultDelegate(Context context, boolean showSpinner) {
            mContext = context;
            mShowSpinner = showSpinner;
        }

        public void onBeforeQuery() {
            if (mShowSpinner) {
                pd = new ProgressDialog(mContext);
                pd.setTitle("Loading...");
                pd.setMessage("Please wait.");
                pd.setCancelable(false);
                pd.show();
            }
        }

        public void onQueryComplete(T result) {
            if (pd != null) {
                pd.dismiss();
            }
        }

        public void onNetworkFailure(T results, int errorMessage) {
            if (pd != null) {
                pd.dismiss();
            }
            Util.displayNetworkErrorToast(mContext);
        }
    }

    private final Handler mIOHandler;
    private final Context mContext;
    private boolean mMainCategoriesLoaded = false;
    private boolean mSubCategoriesLoaded = false;
    private ArrayList<Runnable> mTasksPendingCredentials;

    private User mUser;
    private List<PledgeCard> mCreditCards;

    public PledgeModel(Context context) {
        mContext = context;

        // Create a new background thread for processing I/O
        HandlerThread mIOHandlerThread = new HandlerThread("IOHandlerThread");

        // Starts the background thread
        mIOHandlerThread.start();
        // Create a handler attached to the HandlerThread's Looper
        mIOHandler = new Handler(mIOHandlerThread.getLooper());

        mTasksPendingCredentials = new ArrayList<>();
    }

    public void createOrFindUser(String email, String firstName, String lastName) {
        AsyncHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mUser = User.fromJson(response);
                runBlockingTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: Handle failure
            }
        };

        PledgeClient.getInstance().createOrFindUser(email, firstName, lastName, handler);
    }

    public void addCreditCard(CreditCard creditCard, AsyncHttpResponseHandler handler) {
        PledgeClient.getInstance().addCreditCard(mUser.getEmail(), creditCard, handler);
    }

    public void donate(double amount, NonProfit nonProfit, AsyncHttpResponseHandler handler) {
        String cardId = mCreditCards.isEmpty() ? "" : mCreditCards.get(0).getId();
        PledgeClient.getInstance().donate(mUser.getUserId(), cardId, amount, nonProfit, handler);
    }

    public void getMajorCategories() {
        AsyncHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                NonProfit.saveMajorCategoryInfoFromJSON(response);
                mMainCategoriesLoaded = true;
                runBlockingTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: Handle failure
            }
        };

        PledgeClient.getInstance().getMajorCategories(handler);
    }

    public void getSubcategories() {
        AsyncHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                NonProfit.saveSubCategoryInfoFromJSON(response);
                mSubCategoriesLoaded = true;
                runBlockingTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: Handle failure
            }
        };

        PledgeClient.getInstance().getSubcategories(handler);
    }

    private void runBlockingTasks() {
        if (bootstrapComplete()) {
            for (Runnable task : mTasksPendingCredentials) {
                mIOHandler.post(task);
            }
        }
    }


    private void postWhenBootstrapComplete(Runnable task) {
        if (bootstrapComplete()) {
            mIOHandler.post(task);
        } else {
            mTasksPendingCredentials.add(task);
        }
    }

    private boolean bootstrapComplete() {
        return mMainCategoriesLoaded && mSubCategoriesLoaded && mUser != null;
    }


    public void getFeatured(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        postWhenBootstrapComplete(new GetFeaturedTask(delegate));
    }

    public void getLocal(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        postWhenBootstrapComplete(new GetLocalTask(delegate));
    }

    public void getCreditCards(boolean forceServerFetch, PledgeModel.OnResultDelegate<List<PledgeCard>> delegate) {
        postWhenBootstrapComplete(new GetCreditCardsTask(forceServerFetch, delegate));
    }

    public void getDonationHistory(PledgeModel.OnResultDelegate<List<Donation>> delegate) {
        postWhenBootstrapComplete(new GetDonationHistoryTask(delegate));
    }

    public void search(String query, NonProfit.CategoryInfo category, OnResultDelegate<List<NonProfit>> delegate) {
        search(query, category, 1 /*page*/, delegate);
    }

    public void search(String query, NonProfit.CategoryInfo category, int page, OnResultDelegate<List<NonProfit>> delegate) {
        postWhenBootstrapComplete(new SearchQueryTask(query, category, page, delegate));
    }

    private class GetFeaturedTask extends GetQueryTask<List<NonProfit>> {
        public GetFeaturedTask(OnResultDelegate<List<NonProfit>> delegate) {
            super(delegate);
        }

        @Override
        protected List<NonProfit> getLocalResult() {
            return null;
        }

        @Override
        protected boolean shouldSkipRemoteQuery(List<NonProfit> localResult) {
            return false;
        }

        @Override
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().getLocal(httpResponseHandler);
        }

        @Override
        protected List<NonProfit> parseRemoteResult(Context context, JSONArray resultJSON) {
            return NonProfit.fromJSONArray(resultJSON);
        }
    }

    private class GetLocalTask extends GetQueryTask<List<NonProfit>> {
        public GetLocalTask(OnResultDelegate<List<NonProfit>> delegate) {
            super(delegate);
        }

        @Override
        protected List<NonProfit> getLocalResult() {
            return null;
        }

        @Override
        protected boolean shouldSkipRemoteQuery(List<NonProfit> localResult) {
            return false;
        }

        @Override
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().getLocal(httpResponseHandler);
        }

        @Override
        protected List<NonProfit> parseRemoteResult(Context context, JSONArray resultJSON) {
            return NonProfit.fromJSONArray(resultJSON);
        }
    }

    private class GetCreditCardsTask extends GetQueryTask<List<PledgeCard>> {
        private boolean mForceFetchFromServer;

        public GetCreditCardsTask(boolean forceFetchFromServer, OnResultDelegate<List<PledgeCard>> delegate) {
            super(delegate);
            mForceFetchFromServer = forceFetchFromServer;
        }

        @Override
        protected List<PledgeCard> getLocalResult() {
            return mCreditCards;
        }

        @Override
        protected boolean shouldSkipRemoteQuery(List<PledgeCard> localResult) {
            return !mForceFetchFromServer && mCreditCards != null;
        }

        @Override
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().getCreditCards(mUser.getUserId(), httpResponseHandler);
        }

        @Override
        protected List<PledgeCard> parseRemoteResult(Context context, JSONArray resultJSON) {
            ArrayList<PledgeCard> creditCards = new ArrayList<>();

            for (int i = 0; i < resultJSON.length(); i++) {
                try {
                    creditCards.add(PledgeCard.fromJson(resultJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mCreditCards = creditCards;

            return creditCards;
        }
    }

    private class GetDonationHistoryTask extends GetQueryTask<List<Donation>> {

        public GetDonationHistoryTask(OnResultDelegate<List<Donation>> delegate) {
            super(delegate);
        }

        @Override
        protected List<Donation> getLocalResult() {
            return null;
        }

        @Override
        protected boolean shouldSkipRemoteQuery(List<Donation> localResult) {
            return false;
        }

        @Override
        protected void fetchRemoteResult(JsonHttpResponseHandler handler) {
            PledgeClient.getInstance().getDonationHistory(mUser.getUserId(), handler);
        }

        @Override
        protected List<Donation> parseRemoteResult(Context context, JSONArray resultJSON) {
            return Donation.fromJsonArray(resultJSON);
        }
    }

    private class SearchQueryTask extends GetQueryTask<List<NonProfit>> {
        private final String mQuery;
        private final NonProfit.CategoryInfo mCategory;
        private final int mPage;

        public SearchQueryTask(String query,
                               NonProfit.CategoryInfo category,
                               int page,
                               OnResultDelegate<List<NonProfit>> delegate) {
            super(delegate);

            mQuery = query;
            mCategory = category;
            mPage = page;
        }

        @Override
        protected List<NonProfit> getLocalResult() {
            return null;
        }

        @Override
        protected boolean shouldSkipRemoteQuery(List<NonProfit> localResult) {
            return false;
        }

        @Override
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().search(mQuery, mCategory, mPage, httpResponseHandler);
        }

        @Override
        protected List<NonProfit> parseRemoteResult(Context context, JSONArray resultJSON) {
            return NonProfit.fromJSONArray(resultJSON);
        }
    }

    private abstract class GetQueryTask<T> implements Runnable {
        protected abstract T getLocalResult();
        protected abstract boolean shouldSkipRemoteQuery(T localResult);
        protected abstract void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler);
        protected abstract T parseRemoteResult(Context context, JSONArray resultJSON);

        private OnResultDelegate<T> mDelegate;

        // Defines a Handler object that's attached to the creating thread. The response
        // task is posted to this handler
        private Handler mResponseHandler;

        public GetQueryTask(OnResultDelegate<T> delegate) {
            mDelegate = delegate;
            mResponseHandler = new Handler();
        }

        public void run() {
            mDelegate.onBeforeQuery();

            final T localResults = getLocalResult();

            boolean isNetworkAvailable = Util.isNetworkAvailable(mContext);
            if (!isNetworkAvailable || shouldSkipRemoteQuery(localResults)) {
                if (!isNetworkAvailable && !shouldSkipRemoteQuery(localResults)){
                    postFailure(localResults, R.string.network_error);
                } else {
                    postSuccess(localResults);
                }
                return;
            }

            fetchRemoteResult(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    postSuccess(parseRemoteResult(mContext, response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    int errorMessage = generalFailureResponse(errorResponse);
                    postFailure(localResults, errorMessage);
                }
            });
        }

        private void postSuccess(final T result) {
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDelegate.onQueryComplete(result);
                }
            });
        }

        private void postFailure(final T result, final int errorMessage) {
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDelegate.onNetworkFailure(result, errorMessage);
                }
            });
        }


        private int generalFailureResponse(JSONObject errorResponse) {
            int errorMessage = R.string.network_error;
            try {
                // TODO: Parse more specific info from error response?
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (errorResponse != null) {
                Log.e("DEBUG", errorResponse.toString());
            }

            return errorMessage;
        }
    }
}
