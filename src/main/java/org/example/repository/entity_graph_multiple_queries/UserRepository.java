package org.example.repository.entity_graph_multiple_queries;

import java.util.List;

import org.example.model.entity_graph_multiple_queries.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    //language=sql
    @Query(
      value = "SELECT * FROM users FOR UPDATE SKIP LOCKED;",
      nativeQuery = true
    )
    List<User> findBySomething(String name, Pageable pageable);
}
