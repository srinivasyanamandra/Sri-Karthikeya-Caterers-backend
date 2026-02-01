package sri.karthikeya.caterers.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sri.karthikeya.caterers.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final MongoTemplate mongoTemplate;

    public Review save(Review review) {
        return mongoTemplate.save(review);
    }

    public Optional<Review> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Review.class));
    }

    public List<Review> findAll(int page, int size, String sortBy, String sortDir) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        query.skip((long) page * size);
        query.limit(size);
        return mongoTemplate.find(query, Review.class);
    }

    public long count() {
        return mongoTemplate.count(new Query(), Review.class);
    }

    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Review.class);
    }

    public boolean existsByImageId(String imageId) {
        Query query = new Query(Criteria.where("imageId").is(imageId));
        return mongoTemplate.exists(query, Review.class);
    }

    public boolean existsByImageIdAndIdNot(String imageId, String id) {
        Query query = new Query(Criteria.where("imageId").is(imageId).and("_id").ne(id));
        return mongoTemplate.exists(query, Review.class);
    }
}
