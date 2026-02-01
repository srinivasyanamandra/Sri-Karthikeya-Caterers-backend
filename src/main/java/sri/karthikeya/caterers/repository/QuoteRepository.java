package sri.karthikeya.caterers.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sri.karthikeya.caterers.entity.Quote;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuoteRepository {
    private final MongoTemplate mongoTemplate;

    public Quote save(Quote quote) {
        return mongoTemplate.save(quote);
    }

    public Optional<Quote> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Quote.class));
    }

    public List<Quote> findAll(int page, int size, String sortBy, String sortDir) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        query.skip((long) page * size);
        query.limit(size);
        return mongoTemplate.find(query, Quote.class);
    }

    public long count() {
        return mongoTemplate.count(new Query(), Quote.class);
    }

    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Quote.class);
    }
}
