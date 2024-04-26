package net.javaguides.cms.Enums;
/**
 * Represents the possible statuses of a client's profiles.
 * This enumeration defines the following status types:
 * <ul>
 *     <li>{@link #Approved} - Indicates that the profile has been approved by the employee.</li>
 *     <li>{@link #Pending} - Indicates that the profile currently pending review from the employee.</li>
 *     <li>{@link #Rejected} - Indicates that the profile has been rejected due to some errors or inconsistencies.</li>
 * </ul>
 */
public enum Status {
  Approved, Pending, Rejected
}
