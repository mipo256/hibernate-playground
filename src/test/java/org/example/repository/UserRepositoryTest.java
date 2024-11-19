package org.example.repository;

import org.example.AbstractIntegrationTest;
import org.example.repository.entity_graph_multiple_queries.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@Sql(
  //language=sql
  statements = """
        CREATE TABLE IF NOT EXISTS users (
            id BIGINT PRIMARY KEY,
            name VARCHAR
        );
      """,
  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Generated SQL:
     *     SELECT
     *         *
     *     FROM
     *         users
     *     offset
     *         ? rows
     *     fetch
     *         next ? rows only FOR UPDATE SKIP LOCKED;
     */
    @Test
    void test_withoutSort() {
        userRepository.findBySomething("another", PageRequest.of(1, 10));
    }

    /**
     *     SELECT
     *         *
     *     FROM
     *         users
     *     offset
     *         ? rows
     *     fetch
     *         next ? rows only FOR UPDATE SKIP LOCKED;
     *     order by
     *         FOR.name asc
     */
    @Test
    void test_withSort() {
        userRepository.findBySomething("another", PageRequest.of(1, 10, Sort.by("name")));
    }
}
