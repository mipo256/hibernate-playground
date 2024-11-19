package org.example.repository;

import java.util.List;

import org.example.AbstractIntegrationTest;
import org.example.model.loading.Computer;
import org.example.model.loading.Employee;
import org.example.repository.loading.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.support.TransactionTemplate;

@Sql(
  //language=sql
  statements = """
      CREATE TABLE IF NOT EXISTS employee(
        ID BIGSERIAL PRIMARY KEY,
        NAME TEXT
      );
      CREATE TABLE IF NOT EXISTS computer(
        ID BIGSERIAL PRIMARY KEY,
        MODEL TEXT,
        EMPLOYEE_ID BIGINT REFERENCES employee(id)
      );
      """,
  executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
  //language=sql
  statements = """
      DROP TABLE IF EXISTS computer;
      DROP TABLE IF EXISTS employee;
      """,
  executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class EmployeeRepositoryTest extends AbstractIntegrationTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Test
  void testJoinFetch() {
    Employee employee = employeeRepository.save(
        new Employee()
            .setComputers(
                List.of(
                    new Computer().setModel("model")
                )
            )
    );

    Employee byId = employeeRepository.findOneWithComputers(employee.getId()).get();

    System.out.println(byId.getComputers().getClass().getName());
  }

  @Test
  void testEntityGraph() {
    Employee employee = employeeRepository.save(
        new Employee()
            .setComputers(
                List.of(
                    new Computer().setModel("model")
                )
            )
    );

    Employee byId = employeeRepository.findOneWithComputersViaEntityGraph(employee.getId()).get();

    System.out.println(byId.getComputers().getClass().getName());
  }

  @Test
  void testPaginationWithJoinFetch() {
    employeeRepository.saveAll(
      List
        .of(
          new Employee().setName("Alex").setComputers(List.of(new Computer().setModel("model 1"))),
          new Employee().setName("Albert").setComputers(List.of(new Computer().setModel("model 2")))
        )
    );

    Slice<Employee> al = employeeRepository.findByNameWithComputers("Al", Pageable.ofSize(1));

    System.out.println(al.getContent());
  }

  @Test
  void testLazyLoading() {
    transactionTemplate.executeWithoutResult(status -> {
      employeeRepository.saveAll(
        List
          .of(
            new Employee().setName("Alex").setComputers(List.of(new Computer().setModel("model 1"))),
            new Employee().setName("Albert").setComputers(List.of(new Computer().setModel("model 2"))),
            new Employee().setName("Arnold").setComputers(List.of(new Computer().setModel("model 3"))),
            new Employee().setName("Joseph").setComputers(List.of(new Computer().setModel("model 4"))),
            new Employee().setName("Eugen").setComputers(List.of(new Computer().setModel("model 5"))),
            new Employee().setName("Mark").setComputers(List.of(new Computer().setModel("model 6")))
          )
      );
    });

    transactionTemplate.executeWithoutResult(status -> {
      List<Employee> employee = employeeRepository.findAll();

      employee.forEach(empl -> {
        System.out.println(empl.getComputers().size());
      });
    });
  }
}