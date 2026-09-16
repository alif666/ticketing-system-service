package com.pridesys.ticketing.dto;

import com.pridesys.ticketing.entity.UserRole;
import jakarta.validation.constraints.*;

public final class UsersProjectsDtos {
    private UsersProjectsDtos() {}
    public record CreateUserRequest(@Email @NotBlank String email, @NotBlank @Size(max=120) String name, @NotNull UserRole role, Long clientId) {}
    public record UpdateUserRequest(@NotBlank @Size(max=120) String name, @Size(max=30) String mobile, @Size(max=120) String designation, @Size(max=120) String office, Boolean active) {}
    public record UserResponse(long id,String email,String role,String name,String mobile,String designation,String office,boolean active,Long clientId) {}
    public record CreateClientRequest(@NotBlank @Size(max=160) String name) {}
    public record ClientResponse(long id,String name,boolean active) {}
    public record CreateProjectRequest(@NotBlank @Size(max=160) String name,@NotBlank @Size(max=30) String shortCode,@Size(max=1000) String description) {}
    public record UpdateProjectRequest(@NotBlank @Size(max=160) String name,@Size(max=1000) String description,Boolean active) {}
    public record ProjectResponse(long id,String name,String shortCode,String description,boolean active) {}
    public record CreateModuleRequest(@NotBlank @Size(max=160) String name,@Size(max=1000) String description) {}
    public record UpdateModuleRequest(@NotBlank @Size(max=160) String name,@Size(max=1000) String description,Boolean active) {}
    public record ModuleResponse(long id,long projectId,String name,String description,boolean active) {}
}
