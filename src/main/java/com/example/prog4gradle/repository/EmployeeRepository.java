package com.example.prog4gradle.repository;

import com.example.prog4gradle.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Ajouter la nouvelle méthode de recherche personnalisée
    List<Employee> findByFirstNameContainingAndLastNameContainingAndSexContainingAndJobTitleContainingAndHireDateBetweenAndDepartureDateBetween(
            String firstName, String lastName, String sex, String jobTitle,
            LocalDate hireDateFrom, LocalDate hireDateTo,
            LocalDate departureDateFrom, LocalDate departureDateTo);

    @Query("SELECT e FROM Employee e " +
            "WHERE (:firstName IS NULL OR e.firstName LIKE %:firstName%) " +
            "AND (:lastName IS NULL OR e.lastName LIKE %:lastName%) " +
            "AND (:sex IS NULL OR e.sex = :sex) " +
            "AND (:jobTitle IS NULL OR e.jobTitle LIKE %:jobTitle%) " +
            "AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) " +
            "AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo) " +
            "AND (:departureDateFrom IS NULL OR e.departureDate >= :departureDateFrom) " +
            "AND (:departureDateTo IS NULL OR e.departureDate <= :departureDateTo)")
    List<Employee> findByFilters(@Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("sex") String sex,
                                 @Param("jobTitle") String jobTitle,
                                 @Param("hireDateFrom") LocalDate hireDateFrom,
                                 @Param("hireDateTo") LocalDate hireDateTo,
                                 @Param("departureDateFrom") LocalDate departureDateFrom,
                                 @Param("departureDateTo") LocalDate departureDateTo);

}