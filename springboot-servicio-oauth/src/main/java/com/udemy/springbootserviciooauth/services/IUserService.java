package com.udemy.springbootserviciooauth.services;

import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
    User findByUsername(String username);
    User update( User user, Long id);
}
