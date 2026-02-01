package sri.karthikeya.caterers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sri.karthikeya.caterers.dto.request.QuoteRequest;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.QuoteResponse;
import sri.karthikeya.caterers.entity.Quote;
import sri.karthikeya.caterers.exception.custom.ResourceNotFoundException;
import sri.karthikeya.caterers.mapper.QuoteMapper;
import sri.karthikeya.caterers.repository.QuoteRepository;
import sri.karthikeya.caterers.service.QuoteService;
import sri.karthikeya.caterers.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    @Override
    public QuoteResponse create(QuoteRequest request) {
        log.info("Creating quote for: {}", request.getFullName());
        
        Quote quote = quoteMapper.toEntity(request);
        quote.setId(UUID.randomUUID().toString());
        quote.setCreatedAt(LocalDateTime.now());
        quote.setUpdatedAt(LocalDateTime.now());
        
        Quote saved = quoteRepository.save(quote);
        log.info("Quote created with id: {}", saved.getId());
        return quoteMapper.toResponse(saved);
    }

    @Override
    public QuoteResponse getById(String id) {
        log.info("Fetching quote with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + id));
        return quoteMapper.toResponse(quote);
    }

    @Override
    public PageResponse<QuoteResponse> getAll(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all quotes - page: {}, size: {}", page, size);
        ValidationUtil.validatePagination(page, size);
        
        List<Quote> quotes = quoteRepository.findAll(page, size, sortBy, sortDir);
        long total = quoteRepository.count();
        
        List<QuoteResponse> responses = quotes.stream()
                .map(quoteMapper::toResponse)
                .toList();
        
        return PageResponse.<QuoteResponse>builder()
                .content(responses)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .last(page >= (int) Math.ceil((double) total / size) - 1)
                .build();
    }

    @Override
    public QuoteResponse update(String id, QuoteRequest request) {
        log.info("Updating quote with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + id));
        
        quoteMapper.updateEntity(request, quote);
        quote.setUpdatedAt(LocalDateTime.now());
        
        Quote updated = quoteRepository.save(quote);
        log.info("Quote updated with id: {}", updated.getId());
        return quoteMapper.toResponse(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting quote with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        if (!quoteRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Quote not found with id: " + id);
        }
        
        quoteRepository.deleteById(id);
        log.info("Quote deleted with id: {}", id);
    }
}
