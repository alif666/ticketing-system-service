package com.pridesys.ticketing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;

@Component
public class LocalSeedData implements CommandLineRunner {
    private final UserRepository users;
    private final ClientRepository clients;
    private final ProjectRepository projects;
    private final ModuleRepository modules;
    private final ProjectMembershipRepository memberships;
    private final PasswordEncoder encoder;
    private final boolean enabled;

    public LocalSeedData(UserRepository u, ClientRepository c, ProjectRepository p, ModuleRepository m,
                         ProjectMembershipRepository pm, PasswordEncoder e,
                         @Value("${app.seed.enabled:true}") boolean en) {
        users = u;
        clients = c;
        projects = p;
        modules = m;
        memberships = pm;
        encoder = e;
        enabled = en;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) return;
        var acme = clients.findByName("Acme Corporation").orElseGet(() -> clients.save(new ClientEntity("Acme Corporation")));
        seed("app.admin@example.com", UserRole.APP_ADMIN, "Application Admin", "01700000001", "Company Administrator", null);
        seed("client.admin@example.com", UserRole.CLIENT_ADMIN, "Client Administrator", "01700000002", "Client Administrator", acme.getId());
        seed("client.user@example.com", UserRole.CLIENT_USER, "Client User", "01700000003", "Support User", acme.getId());
        seedDemoWorkspace();
    }

    private void seed(String email, UserRole role, String name, String mobile, String designation, Long client) {
        var u = users.findByEmailIgnoreCase(email).orElseGet(() -> new UserEntity(email, encoder.encode("Password123!"), role, name, client));
        u.setPasswordHash(encoder.encode("Password123!"));
        u.setRole(role);
        u.setName(name);
        u.setMobile(mobile);
        u.setDesignation(designation);
        u.setOffice("Dhaka");
        u.setActive(true);
        u.setClientId(client);
        users.save(u);
    }

    private void seedDemoWorkspace() {
        var project = projects.findByShortCode("DEMO").orElseGet(() -> projects.save(
                new ProjectEntity("Customer Support Portal", "DEMO", "Seeded workspace for the ticketing demo")));
        seedModule(project, "Authentication & Access", "Login, password reset, and account access issues.");
        seedModule(project, "Ticket Submission", "Issue intake, validation, and reporter experience.");
        addMember(project, "client.admin@example.com");
        addMember(project, "client.user@example.com");
    }

    private void seedModule(ProjectEntity project, String name, String description) {
        modules.findByProjectIdAndName(project.getId(), name).orElseGet(() -> modules.save(
                new ModuleEntity(project.getId(), name, description)));
    }

    private void addMember(ProjectEntity project, String email) {
        users.findByEmailIgnoreCase(email).ifPresent(user -> {
            var membership = new ProjectMembershipId(project.getId(), user.getId());
            if (!memberships.existsById(membership)) memberships.save(new ProjectMembershipEntity(project.getId(), user.getId()));
        });
    }
}
