package com.phunghv.base.persistent.primary.repo;

import com.phunghv.base.persistent.primary.entity.DatabaseMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseMappingRepo extends JpaRepository<DatabaseMapping, Long> {


    List<DatabaseMapping> findAllByActive(Boolean active);

    DatabaseMapping findFirstByDbKeyAndActive(String dbKey, Boolean active);
}
