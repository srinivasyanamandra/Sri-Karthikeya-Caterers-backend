package sri.karthikeya.caterers.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sri.karthikeya.caterers.entity.Menu;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuRepository {
    private final MongoTemplate mongoTemplate;

    public Menu save(Menu menu) {
        return mongoTemplate.save(menu);
    }

    public Optional<Menu> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Menu.class));
    }

    public List<Menu> findAll(int page, int size, String sortBy, String sortDir) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        query.skip((long) page * size);
        query.limit(size);
        return mongoTemplate.find(query, Menu.class);
    }

    public long count() {
        return mongoTemplate.count(new Query(), Menu.class);
    }

    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Menu.class);
    }

    public boolean existsByImageId(String imageId) {
        Query query = new Query(Criteria.where("imageId").is(imageId));
        return mongoTemplate.exists(query, Menu.class);
    }

    public boolean existsByImageIdAndIdNot(String imageId, String id) {
        Query query = new Query(Criteria.where("imageId").is(imageId).and("_id").ne(id));
        return mongoTemplate.exists(query, Menu.class);
    }
}
