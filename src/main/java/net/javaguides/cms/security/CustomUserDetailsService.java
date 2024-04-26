package net.javaguides.cms.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import net.javaguides.cms.entity.Client;
import net.javaguides.cms.entity.Employee;
import net.javaguides.cms.repository.ClientRepository;
import net.javaguides.cms.repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * Custom service for user details that implements Spring Security's UserDetailsService.
 *
 * This service provides a method to load user details based on a username or email address.
 * It first attempts to find an employee by the username or email; if not found, it tries to find a client.
 * This approach supports authentication for both employees and clients within the same system.
 *
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


  private EmployeeRepository employeeRepository;
  private ClientRepository clientRepository;

  /**
   * Loads the user's details based on the username or email provided.
   *
   * This method first checks the {@link EmployeeRepository} for a user matching the username or email.
   * If an employee is found, it constructs a {@link UserDetails} object with roles converted to {@link GrantedAuthority}.
   * If no employee is found, it then checks the {@link ClientRepository}.
   * If a client is found, it similarly constructs a {@link UserDetails} object.
   * If neither repository contains the user, it throws a {@link UsernameNotFoundException}.
   *
   * @param usernameOrEmail The username or email of the user to load.
   * @return UserDetails containing the user's information and authorities.
   * @throws UsernameNotFoundException if no user is found in either repository.
   */


  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    //create UserDetails object to return later
    //check if there is an employee
    Optional<Employee> employee = employeeRepository.findByUsernameOrEmail(usernameOrEmail,
        usernameOrEmail);
    if (employee.isPresent()) {
      //retrieve employee if present
      Employee employeeReceived = employee.get();

      //create list of granted authorities
      Set<GrantedAuthority> grantedAuthorities = employeeReceived.getRoles().stream()
          .map(role -> new SimpleGrantedAuthority(role.getName()))
          .collect(Collectors.toSet());


      return new org.springframework.security.core.userdetails.User(
          employeeReceived.getUsername(),
          employeeReceived.getPassword(),
          grantedAuthorities
      );

    } else {
      // If not found, try to load the user as a client
      Client client = clientRepository.findByUsername(usernameOrEmail)
          //if no username is found, then it means they are not in the CMS system
          .orElseThrow(() -> new UsernameNotFoundException(
              "User not found with username: " + usernameOrEmail));
      //create list of granted authorities
      Set<GrantedAuthority> grantedAuthorities = client.getRoles().stream()
          .map(role -> new SimpleGrantedAuthority(role.getName()))
          .collect(Collectors.toSet());

      //return the user
      return new org.springframework.security.core.userdetails.User(
          client.getUsername(),
          client.getPassword(),
          grantedAuthorities
      );

    }
  }

}