package net.javaguides.cms.service.Implementations;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import net.javaguides.cms.dto.ClientDTO;
import net.javaguides.cms.dto.EmployeeDto;
import net.javaguides.cms.dto.PasswordChangeDto;
import net.javaguides.cms.entity.Client;
import net.javaguides.cms.entity.Employee;
import net.javaguides.cms.entity.Role;
import net.javaguides.cms.exception.ResourceNotFoundException;
import net.javaguides.cms.mapper.ClientMapper;
import net.javaguides.cms.mapper.EmployeeMapper;
import net.javaguides.cms.repository.EmployeeRepository;
import net.javaguides.cms.repository.RoleRepository;
import net.javaguides.cms.service.EmployeeService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository employeeRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  /**
   * Initializes the admin account at application start-up.
   *
   * It checks if an admin user already exists in the system by the username "admin". If the admin
   * user does not exist, it creates a new admin user with predefined attributes and a strong encoded password.
   * The new admin user is then saved to the repository.
   *
   * If an admin user already exists, the method simply returns without making any changes.
   */

  @PostConstruct
  public void initAdmin() {
    if (employeeRepository.existsByUsername("admin")) {
      // Admin already exists, so do nothing
      return;
    }

    Employee admin = new Employee();

    admin.setUsername("admin");
    admin.setEmail("admin@example.com");
    admin.setFirstName("Admin");
    admin.setLastName("User");
    admin.setPassword(passwordEncoder.encode("strongAdminPassword"));
    admin.setDepartment("IT");
    Role adminRole = roleRepository.findByName("ADMIN");

    admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

    employeeRepository.save(admin);
  }


  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeDto employeeDto) {
    Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

    String initialPassword = "defaultEmployeePassword";  // This should be securely generated or set
    employee.setPassword(passwordEncoder.encode(initialPassword));

    Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE");
    employee.setRoles(new HashSet<>(Arrays.asList(employeeRole)));
    Employee savedEmployee = employeeRepository.save(employee);

    return EmployeeMapper.mapToEmployeeDto(savedEmployee);
  }

  @Override
  public EmployeeDto getEmployeeById(Long employeeId) {
    EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employeeRepository.findById(employeeId).
        orElseThrow(()->new ResourceNotFoundException("Employee does not exist by the given id "
            + employeeId)));

    return employeeDto;
  }

  @Override

  public List<EmployeeDto> getEmployees() {
    List<Employee> employees = employeeRepository.findAll();


    return employees.stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedClient) {
    Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->
        new ResourceNotFoundException("Client does not exist with given id"));

    employee.setFirstName(updatedClient.getFirstName());
    employee.setLastName(updatedClient.getLastName());
    //employee.setUsername(updatedClient.getUsername());
   // employee.setEmail(updatedClient.getEmail());
    employee.setDepartment(updatedClient.getDepartment());
    Employee updatedEmployeeObject = employeeRepository.save(employee);
    return EmployeeMapper.mapToEmployeeDto((updatedEmployeeObject));
  }

  @Override
  @Transactional
  public void deleteEmployee(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
    employee.getRoles().clear(); // Clearing roles associated with the employee
    employeeRepository.save(employee); // Save the employee to update the change

    employeeRepository.deleteById(employeeId);
  }


  @Override
  @Transactional
  public boolean changePassword(Long employeeId, PasswordChangeDto passwordChangeDto) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Check if old password matches
    if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), employee.getPassword())) {
      return false;
    }

    // Set new password
    employee.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
    employeeRepository.save(employee);
    return true;
  }
}
