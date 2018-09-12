package org.iproduct.spring.restmvc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@JsonIgnoreProperties({"authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Length(min = 3, max = 30)
    @NonNull
    private String username;

    @NotNull
    @Length(min = 4, max = 80)
    @NonNull
    private String password;

    @NotNull
    @Length(min = 1, max = 30)
    @NonNull
    private String fname;

    @NotNull
    @Length(min = 1, max = 30)
    @NonNull
    private String lname;

    @NonNull
    @Builder.Default
    private String roles;

    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @JsonFormat(pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime created = LocalDateTime.now();

    @JsonFormat(pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime updated = LocalDateTime.now();

    public User(long id,
                @NotNull @Length(min = 3, max = 30) String username,
                @NotNull @Length(min = 4, max = 80) String password,
                @NotNull @Length(min = 1, max = 30) String fname,
                @NotNull @Length(min = 1, max = 30) String lname,
                String roles,
                boolean active,
                LocalDateTime created,
                LocalDateTime updated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.roles = roles;
        this.active = active;
        this.created = created;
        this.updated = updated;
    }

    public User() {
    }

    @java.beans.ConstructorProperties({"username", "password", "fname", "lname", "roles"})
    public User(@NotNull @Length(min = 3, max = 30) String username,
                @NotNull @Length(min = 4, max = 80) String password,
                @NotNull @Length(min = 1, max = 30) String fname,
                @NotNull @Length(min = 1, max = 30) String lname,
                String roles) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(roles.split("\\s*,\\s*")).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
