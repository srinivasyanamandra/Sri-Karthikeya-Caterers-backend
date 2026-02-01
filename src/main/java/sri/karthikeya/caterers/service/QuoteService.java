package sri.karthikeya.caterers.service;

import sri.karthikeya.caterers.dto.request.QuoteRequest;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.QuoteResponse;

public interface QuoteService {
    QuoteResponse create(QuoteRequest request);
    QuoteResponse getById(String id);
    PageResponse<QuoteResponse> getAll(int page, int size, String sortBy, String sortDir);
    QuoteResponse update(String id, QuoteRequest request);
    void delete(String id);
}
