package com.panasonic.in.comm;

import android.util.Log;

import com.panasonic.in.comm.auth.AuthenticationService;
import com.panasonic.in.comm.quote.Quote;
import com.panasonic.in.comm.quote.QuoteService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Favqs.com
 * 0a2bc0fa68941f19300a1c6dd3311f64
 *
 */
public class Communicator {
    private Retrofit mRetrofit;
    public static Communicator mInstance = null;

    public static synchronized Communicator getInstance() {
        if (null == mInstance) {
            mInstance = new Communicator();
        }
        return mInstance;
    }

    private Communicator() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://favqs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    public AuthenticationService getAuthenticationService() {
        AuthenticationService authService = mRetrofit.create(AuthenticationService.class);
        return authService;
    }

    public QuoteService getQuoteService() {
        QuoteService quoteService = mRetrofit.create(QuoteService.class);
        return quoteService;
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long startTime = System.nanoTime();
            Log.i(Communicator.class.getName(), String.format("Sending request %s on %s",
                    request.url().toString(), request.headers().toString()));

            Response response = chain.proceed(request);

            long endTime = System.nanoTime();
            Log.i(Communicator.class.getName(), String.format("Received response from %s in %.1fms%n%s",
                    response.request().url().toString(), (endTime - startTime) / 1e6d, response.headers().toString()));

            return response;
        }
     }
}
