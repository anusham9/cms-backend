package net.javaguides.cms.repository;

import java.util.Optional;
import net.javaguides.cms.entity.Employee;
import net.javaguides.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  /**
   * Retrieves an Employee entity by its username. This method provides an easy way to fetch user details based on the
   * unique username attribute.
   *
   * @param username the username of the user to be retrieved.
   * @return an {@link Optional} containing the {@link User} if found, or empty if no user exists with the given username.
   */
  Optional<Employee> findByUsernameOrEmail(String username,String email);


  Boolean existsByUsername(String username);
}
