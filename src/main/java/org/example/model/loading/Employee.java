package org.example.model.loading;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Accessors(chain = true)
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.BIGINT)
  private Long id;

  private String name;

  @BatchSize(size = 3)
  @OneToMany(fetch = FetchType.LAZY)
  @Cascade(value = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinColumn(name = "employee_id")
  private List<Computer> computers;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
