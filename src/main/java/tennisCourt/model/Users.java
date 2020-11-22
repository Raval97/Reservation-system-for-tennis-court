package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.service.spi.InjectService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="users")
public class Users implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    @JsonBackReference
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private Client client;
    @JsonBackReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserReservation> userReservations;
    @JsonBackReference
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private ClubAssociation clubAssociation;
    @JsonBackReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<MembershipApplication> membershipApplications;
    @JsonBackReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Payment> payments;
    @JsonBackReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserTournament> userTournaments = new HashSet<>();
    @JsonBackReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<UserTournamentApplication> userTournamentApplications;

    public Users() {
    }

    public Users(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Users(String username, String password, String role, Client client) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.client=client;
        this.client.setUsers(this);
    }

    public void setPassword(String password) {
        this.password = password;
        this.password = passwordEncoder().encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @InjectService
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getUserName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userName = userDetails.getUsername();
        }
        return userName;
    }
}
