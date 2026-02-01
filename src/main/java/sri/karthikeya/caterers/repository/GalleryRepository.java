package sri.karthikeya.caterers.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sri.karthikeya.caterers.entity.Gallery;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GalleryRepository {
    private final MongoTemplate mongoTemplate;

    public Gallery save(Gallery gallery) {
        return mongoTemplate.save(gallery);
    }

    public Optional<Gallery> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Gallery.class));
    }

    public List<Gallery> findAll(int page, int size, String sortBy, String sortDir) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        query.skip((long) page * size);
        query.limit(size);
        return mongoTemplate.find(query, Gallery.class);
    }

    public long count() {
        return mongoTemplate.count(new Query(), Gallery.class);
    }

    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Gallery.class);
    }

    public boolean existsByImageId(String imageId) {
        Query query = new Query(Criteria.where("imageId").is(imageId));
        return mongoTemplate.exists(query, Gallery.class);
    }

    public boolean existsByImageIdAndIdNot(String imageId, String id) {
        Query query = new Query(Criteria.where("imageId").is(imageId).and("_id").ne(id));
        return mongoTemplate.exists(query, Gallery.class);
    }
}
