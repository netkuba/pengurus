package com.pengurus.crm.daos;

import com.pengurus.crm.entities.Quote;

public interface QuoteDAO extends GenericDAO<Quote>{
    
    Quote readQuoteWithJobs(long id);
    
}