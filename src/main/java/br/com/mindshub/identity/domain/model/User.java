package br.com.mindshub.identity.domain.model;

import br.com.mindshub.identity.application.exception.InvalidUserRoleException;
import br.com.mindshub.identity.application.exception.PasswordAlreadyInUseException;
import br.com.mindshub.identity.application.exception.UserAlreadyAdminException;
import br.com.mindshub.identity.application.exception.UserAlreadyTeacherException;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.exception.UserAlreadyActivatedException;
import br.com.mindshub.identity.domain.exception.UserAlreadyDeletedException;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private Long id;
    private UUID uuid;
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updatePassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("The new password must not be empty.");
        }

        if (encodedPassword != null && encodedPassword.equals(this.password)) {
            throw new PasswordAlreadyInUseException("The new password must be different the current");
        }

        this.password = encodedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void promoteToTeacher() {

        if (this.role == Role.TEACHER) {
            throw new UserAlreadyTeacherException("This user is already a teacher.");
        }

        if (role != Role.STUDENT) {
            throw new InvalidUserRoleException("Only students can be promoted to teacher.");
        }

        this.role = Role.TEACHER;
    }

    public void promoteToAdmin() {

        if (this.role == Role.ADMIN) {
            throw new UserAlreadyAdminException("This user is already an admin.");
        }

        if (this.role != Role.TEACHER) {
            throw new InvalidUserRoleException("Only teachers can be promoted to admin.");
        }
        this.role = Role.ADMIN;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void activate() {

        if (this.active) {
            throw new UserAlreadyActivatedException("User account is already activated.");
        }

        this.active = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new UserAlreadyDeletedException("User is already deleted.");
        }

        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
