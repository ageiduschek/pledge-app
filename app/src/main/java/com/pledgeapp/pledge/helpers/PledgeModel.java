package com.pledgeapp.pledge.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeClient;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ageiduschek on 10/23/15.
 */
public class PledgeModel {
    /**
     *
     * @param <T> The query result type
     */
    public interface OnResultDelegate<T> {
        /**
         * Callback when results come back in query.
         * @param result Query result
         */
        void onQueryComplete(T result);

        /**
         *
         * @param errorMessage String describing error
         */
        void onNetworkFailure(int errorMessage);
    }

    private final Handler mIOHandler;
    private final Context mContext;
    private boolean mMainCategoriesLoaded = false;
    private boolean mSubCategoriesLoaded = false;
    private boolean mUserRegistered = false;
    private ArrayList<Runnable> mTasksPendingCredentials;

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
                mUserRegistered = true;
                runBlockingTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: Handle failure
            }
        };

        PledgeClient.getInstance().createOrFindUser(email, firstName, lastName, handler);
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
        return mMainCategoriesLoaded && mSubCategoriesLoaded && mUserRegistered;
    }


    public void getFeatured(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        postWhenBootstrapComplete(new GetFeaturedTask(delegate));
    }

    public void getLocal(PledgeModel.OnResultDelegate<List<NonProfit>> delegate) {
        postWhenBootstrapComplete(new GetLocalTask(delegate));
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
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().getLocal(httpResponseHandler);
        }

        @Override
        protected List<NonProfit> parseRemoteResult(Context context, JSONArray resultJSON) {
            return NonProfit.fromJSONArray(resultJSON);
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
        protected void fetchRemoteResult(JsonHttpResponseHandler httpResponseHandler) {
            PledgeClient.getInstance().search(mQuery, mCategory, mPage, httpResponseHandler);
        }

        @Override
        protected List<NonProfit> parseRemoteResult(Context context, JSONArray resultJSON) {
            return NonProfit.fromJSONArray(resultJSON);
        }
    }

    private abstract class GetQueryTask<T> implements Runnable {
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
            boolean isNetworkAvailable = Util.isNetworkAvailable(mContext);
            if (!isNetworkAvailable ) {
                postFailure(R.string.network_error);
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
                    postFailure(errorMessage);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(mContext, responseString, Toast.LENGTH_LONG).show();
                    if (responseString != null) {
                        Log.d("ASDF", responseString);
                    }
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

        private void postFailure(final int errorMessage) {
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDelegate.onNetworkFailure(errorMessage);
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
