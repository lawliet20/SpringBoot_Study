package com.wwj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * *
 *
 * @author tzz
 * @功能描述
 * @date 2016/5/3
 * 修改人    修改时间   修改说明
 * **
 */
@Service("myUserDetailService")
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

//    @Resource
//    UserDao customUserDao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        logger.info("获取用户detail:{}",username);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User("wwj", "123", list);
    }

    /*
    * 获取当前用户
    * */
    public static String getCurrentUser(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }
}
