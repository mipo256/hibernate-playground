package org.example.repository.loading;

import java.util.Optional;

import org.example.model.loading.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

  //language=sql
  @Query(value = "SELECT e FROM Employee e JOIN FETCH e.computers WHERE e.id = :id")
  Optional<Employee> findOneWithComputers(@Param("id") Long id);

  //language=sql
  @Query(value = "SELECT e FROM Employee e JOIN FETCH e.computers WHERE e.name LIKE '%' || :name || '%'")
  Slice<Employee> findByNameWithComputers(@Param("name") String name, Pageable pageable);

  //language=sql
  Optional<Employee> findByName(@Param("name") String name);

  //language=sql
  @EntityGraph(attributePaths = {"computers"})
  @Query(value = "FROM Employee e WHERE e.id = :id")
  Optional<Employee> findOneWithComputersViaEntityGraph(@Param("id") Long id);

}
