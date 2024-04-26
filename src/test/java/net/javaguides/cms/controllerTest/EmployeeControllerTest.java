package net.javaguides.cms.controllerTest;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import net.javaguides.cms.dto.EmployeeDto;
import net.javaguides.cms.service.EmployeeService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Tests for EmployeeController using Spring's MockMvc framework.
 * This class includes various test cases for testing the behavior of the
 * employee management controller with different roles and permissions.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmployeeService employeeService;


  /**
   * Tests the creation and deletion of an employee by an admin.
   * Verifies if the system correctly handles the POST request to create an
   * employee and then a DELETE request to remove the same employee.
   * Ensures that the status codes for creation is 201 (Created) and for deletion is 200 (OK).
   */

  @Test
  @WithMockUser(roles = "ADMIN") // This mocks a user with 'ADMIN' role
  public void testCreateAndDeleteEmployee() throws Exception {
    // JSON payload for creating the employee
    String employeeJson = """
            {
                "firstName": "Anusha",
                "lastName": "Mannava",
                "username": "mannava.an",
                "email": "amannava@cms.org",
                "department": "HR"
            }
            """;

    // Prepare an employee object to be returned by the mock
    EmployeeDto expectedEmployee = new EmployeeDto();
    expectedEmployee.setId(1L);  // Assuming the employee ID is generated and should be 1
    expectedEmployee.setFirstName("Anusha");
    expectedEmployee.setLastName("Mannava");
    expectedEmployee.setUsername("mannava.an");
    expectedEmployee.setEmail("amannava@cms.org");
    expectedEmployee.setDepartment("HR");

    // Setup the mock to return the expected employee
    when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(expectedEmployee);

    // Perform POST request to create the employee
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cms/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(employeeJson))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    // Extract the employee ID from the response
    String responseContent = result.getResponse().getContentAsString();
    JSONObject jsonResponse = new JSONObject(responseContent);
    Long employeeId = jsonResponse.getLong("id");

    // Perform DELETE request to delete the employee
    mockMvc.perform(MockMvcRequestBuilders.delete("/cms/employees/{id}", employeeId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }


  /**
   * Tests that an unauthorized role (EMPLOYEE) receives a 403 Forbidden status when
   * trying to create an employee.
   * This test verifies that role-based access control is enforced within the application.
   */

  @Test
  @WithMockUser(roles = "EMPLOYEE") // This mocks a user with 'EMPLOYEE' role
  public void testCreateEmployeeError() throws Exception {
    // JSON payload for the test
    String employeeJson = """
            {
                "id": 1,
                "firstName": "John",
                "lastName": "Doe",
                "username": "johndoe",
                "email": "john.doe@example.com",
                "department": "HR"
            }
            """;
    mockMvc.perform(MockMvcRequestBuilders.post("/cms/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(employeeJson))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  /**
   * Tests retrieval of all employees by an admin.
   * Verifies if the GET request returns a list of employees and checks
   * the correctness of the data and the HTTP status code.
   */

  @Test
  @WithMockUser(roles = "ADMIN") // This mocks a user with 'ADMIN' role
  public void getAllEmployees() throws Exception {
    List<EmployeeDto> fakeEmployees = new ArrayList<>();
    fakeEmployees.add(new EmployeeDto(1L, "John", "Doe", "johndoe", "john.doe@example.com", "HR"));
    fakeEmployees.add(new EmployeeDto(2L, "Jane", "Doe", "janedoe", "jane.doe@example.com", "Marketing"));
    fakeEmployees.add(new EmployeeDto(3L, "Jim", "Beam", "jimbeam", "jim.beam@example.com", "Finance"));
    fakeEmployees.add(new EmployeeDto(4L, "Jack", "Daniels", "jackdaniels", "jack.daniels@example.com", "IT"));

    // Set up the mock to return the fake list
    when(employeeService.getEmployees()).thenReturn(fakeEmployees);

    // Perform the request and verify the response
    mockMvc.perform(MockMvcRequestBuilders.get("/cms/employees")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4))) // Ensure the JSON array has 4 items
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[1].firstName").value("Jane"))
        .andExpect(jsonPath("$[2].firstName").value("Jim"))
        .andExpect(jsonPath("$[3].firstName").value("Jack"));
  }

  /**
   * Tests that a client role receives a 403 Forbidden status when trying to
   * retrieve all employees.
   * This test ensures that unauthorized access to sensitive data is prevented.
   */

  @Test
  @WithMockUser(roles = "CLIENT") // This mocks a user with 'CLIENT' role
  public void getAllEmployees_AsClient_Forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/cms/employees"))
        .andExpect(status().isForbidden()); // Expecting forbidden status as the client should not access employee data
  }

  /**
   * Tests the retrieval of a single employee by ID with an authorized role (ADMIN).
   * Verifies that the correct employee data is returned and the HTTP status is 200 (OK).
   */

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetEmployee() throws Exception{
    Long employeeId = 7L; // Specify the ID of the employee to retrieve

    mockMvc.perform(MockMvcRequestBuilders.get("/cms/employees/{id}", employeeId))
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  /**
   * Tests that an EMPLOYEE role receives a 403 Forbidden status when trying to
   * retrieve an employee by ID, verifying that only admins can perform this action.
   */
  @Test
  @WithMockUser(roles = "EMPLOYEE") //generates error - only 'ADMIN' can delete the employee
  public void testGetEmployeeError() throws Exception{
    Long employeeId = 1L; // Specify the ID of the employee to retrieve

    mockMvc.perform(MockMvcRequestBuilders.get("/cms/employees/{id}", employeeId))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

  }

  /**
   * Tests that an EMPLOYEE role receives a 403 Forbidden status when trying to
   * delete an employee, ensuring that only admins can perform this action.
   */
  @Test
  @WithMockUser(roles = "EMPLOYEE") //generates error - only 'ADMIN' can delete the employee
  public void testDeleteEmployeeError() throws Exception {
    Long employeeId = 1L; // Specify the ID of the employee to delete

    mockMvc.perform(MockMvcRequestBuilders.delete("/cms/employees/{id}", employeeId))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  /**
   * Tests the update of an employee's record by an admin.
   * Verifies that the employee data is correctly updated in the system and
   * the response contains the updated information with an HTTP status of 200 (OK).
   */

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateEmployeeRecord() throws Exception {
    Long employeeId = 7L; // Specify the ID of the employee to update
    String updatedEmployeeJson = """
            {
         
                "firstName": "UpdatedFirstName",
                "lastName": "UpdatedLastName",
                "username": "updatedUsername",
                "email": "updated.email@example.com",
                "department": "UpdatedDepartment"
            }
            """;

    // prepare an employee DTO to be returned by the mock
    EmployeeDto updatedEmployee = new EmployeeDto();
    updatedEmployee.setId(employeeId);
    updatedEmployee.setFirstName("UpdatedFirstName");
    updatedEmployee.setLastName("UpdatedLastName");
    updatedEmployee.setUsername("updatedUsername");
    updatedEmployee.setEmail("updated.email@example.com");
    updatedEmployee.setDepartment("UpdatedDepartment");

    // Set up the mock to return the updated employee
    when(employeeService.updateEmployee(any(Long.class), any(EmployeeDto.class))).thenReturn(updatedEmployee);

    mockMvc.perform(put("/cms/employees/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedEmployeeJson))
        .andExpect(status().isOk());
  }

  /**
   * Tests that an EMPLOYEE role cannot update an employee record, expecting a
   * 403 Forbidden HTTP status, ensuring proper role-based access controls.
   */

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  public void testUpdateEmployeeRecordFail() throws Exception {
    Long employeeId = 7L; // Specify the ID of the employee to update
    String updatedEmployeeJson = """
        {
     
            "firstName": "UpdatedFirstName",
            "lastName": "UpdatedLastName",
            "username": "updatedUsername",
            "email": "updated.email@example.com",
            "department": "UpdatedDepartment"
        }
        """;

    mockMvc.perform(put("/cms/employees/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedEmployeeJson))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }


  /**
   * Tests password change functionality for an EMPLOYEE role.
   * Verifies that the password is updated successfully and the system returns a success message.
   */

  @Test
  @WithMockUser(roles = {"EMPLOYEE"})
  public void testChangePassword() throws Exception {
    Long employeeId = 5L; // Specify the ID of the employee
    String newPassword = "newPassword123"; // Specify the new password
    String passwordChangeDtoJson =
        "{ \"oldPassword\": \"defaultEmployeePassword\", \"newPassword\": \"" + newPassword + "\" }";

    // Mock the service layer to simulate a successful password change
    when(employeeService.changePassword(eq(employeeId), any())).thenReturn(true);

    mockMvc.perform(patch("/cms/employees/{id}/change-password", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(passwordChangeDtoJson))
        .andExpect(status().isOk())
        .andExpect(content().string("Password updated successfully"));
  }


  /**
   * Tests that a CLIENT role receives an HTTP 400 Bad Request when attempting
   * to change a password, testing for proper access control.
   */

  @Test
  @WithMockUser(roles = "CLIENT") //generates error - only 'EMPLOYEE' can change password
  public void testChangePasswordFail() throws Exception {
    Long employeeId = 7L; // Specify the ID of the employee
    String newPassword = "newPassword"; // Specify the new password
    String passwordChangeDtoJson =
        "{ \"oldPassword\": \"newPassword123\", \"newPassword\": \"" + newPassword
            + "\" }";

    mockMvc.perform(patch("/cms/employees/{id}/change-password", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(passwordChangeDtoJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

}
