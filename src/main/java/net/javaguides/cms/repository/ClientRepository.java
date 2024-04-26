package net.javaguides.cms.repository;

import java.util.Optional;
import net.javaguides.cms.entity.Client;
import net.javaguides.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for {@link Client} entities. This interface handles data access operations for clients
 * within the system's database. It extends {@link JpaRepository}, providing CRUD operations and additional
 * JPA-specific functionality for the client entities.
 *
 * <p>The use of this repository allows abstraction of lower-level database access into simple method calls
 * that ensure consistent and efficient interactions with the database. Common operations include finding,
 * saving, and deleting client records. More complex queries can be added as needed by simply declaring methods
 * following Spring Data JPA conventions or using the {@link org.springframework.data.jpa.repository.Query} annotation.
 *
 * <p>This repository is part of the persistence layer, which directly interacts with the database to perform
 * operations on {@link Client} data. It leverages Spring Data JPA's automatic query derivation from method names,
 * significantly reducing the boilerplate code required for data access.
 *
 * @author An interface that provides CRUD operations for {@link Client} entities.
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see net.javaguides.cms.entity.Client
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

  /**
   * Retrieves a Client entity by its username. This method provides an easy way to fetch user details based on the
   * unique username attribute.
   *
   * @param username the username of the user to be retrieved.
   * @return an {@link Optional} containing the {@link User} if found, or empty if no user exists with the given username.
   */
  Optional<Client> findByUsername(String username);


}
