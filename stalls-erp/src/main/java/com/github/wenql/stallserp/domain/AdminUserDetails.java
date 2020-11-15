package com.github.wenql.stallserp.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.wenql.stallserp.modules.sys.model.SysResource;
import com.github.wenql.stallserp.modules.sys.model.SysAdmin;

/**
 * SpringSecurity需要的用户详情
 */
public class AdminUserDetails implements UserDetails {
    private SysAdmin SysAdmin;
    private List<SysResource> resourceList;
    public AdminUserDetails(SysAdmin SysAdmin, List<SysResource> resourceList) {
        this.SysAdmin = SysAdmin;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return SysAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return SysAdmin.getUsername();
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
        return SysAdmin.getStatus().equals(1);
    }
}
