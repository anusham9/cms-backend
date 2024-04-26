package net.javaguides.cms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
/**
 * Data Transfer Object for handling password change requests.
 * This class is used to capture and transfer new password data securely. It enforces validation rules to ensure
 * that both old and new passwords are provided and that the new password meets certain security requirements.
 * The class is designed to ensure data integrity and security during password updates.
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class PasswordChangeDto {
  /**
   * The current password of the user. This field must not be blank.
   */
  @NotBlank(message = "Old password is required")
  private String oldPassword;

  /**
   * The new password to replace the old one. This field must not be blank and must be at least 8 characters long.
   */

  @NotBlank(message = "New password is required")
  @Size(min = 8, message = "New password must be at least 8 characters long")
  private String newPassword;
}
