package org.example.config;


import lombok.Getter;
import lombok.Setter;
import org.example.entity.Profile;
import org.example.enums.GeneralStatus;
import org.springframework.boot.context.config.Profiles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private Profile profile;

    public CustomUserDetails(Profile profile) {
        this.profile = profile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roleList = new LinkedList<>();
        roleList.add(new SimpleGrantedAuthority(profile.getRoles().name()));
        return roleList;
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return profile.getStatus().equals(GeneralStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
