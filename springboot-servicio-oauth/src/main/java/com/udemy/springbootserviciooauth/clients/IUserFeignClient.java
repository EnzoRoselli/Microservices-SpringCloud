package com.udemy.springbootserviciooauth.clients;

import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("servicio-usuarios")
/*con @FeignClient ya pasa a ser un Component de Spring, asi que se puede inyectar con @Autowired*/
public interface IUserFeignClient {

    @GetMapping("/users/search/user")
    public User findByUsername(@RequestParam String username);

    @PutMapping("users/{id}")
    public User update(@RequestBody User user, @PathVariable Long id);
}
