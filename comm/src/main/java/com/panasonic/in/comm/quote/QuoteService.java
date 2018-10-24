package com.panasonic.in.comm.quote;

import java.util.List;

import io.reactivex.Single;

public interface QuoteService {
    Single<Quote> getQuote();

    Single<List<Quote>> getQuotes();

    Single<Quote> getQotd();
}
