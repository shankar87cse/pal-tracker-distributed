package io.pivotal.pal.tracker.allocations;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisProjectInfoRepository extends CrudRepository<ProjectInfo, String> {}
