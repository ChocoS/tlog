package com.pwawrzyniak.tlog.backend.service.security;

import com.pwawrzyniak.tlog.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserAwareUserDetails implements UserDetails {

  private final User user;
  private final Collection<? extends GrantedAuthority> grantedAuthorities;

  public UserAwareUserDetails(User user) {
    this(user, new ArrayList<>());
  }

  public UserAwareUserDetails(User user, Collection<? extends GrantedAuthority> grantedAuthorities) {
    this.user = user;
    this.grantedAuthorities = grantedAuthorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
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
    return user.isEnabled();
  }

  public User getUser() {
    return user;
  }
}