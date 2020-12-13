package com.markerhub.controller;


import com.markerhub.common.lang.Result;
import com.markerhub.entity.User;
import com.markerhub.entity.ZyjUser;
import com.markerhub.service.UserService;
import com.markerhub.service.ZyjUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ZyjUserService zyjUserService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        return Result.succ(user);
    }

    @PostMapping("/add")
    public Result add(@RequestBody ZyjUser zyjUser){
        return zyjUserService.addZyjUser(zyjUser);
    }

    @GetMapping("/zyjUsers")
    public Result findUsers(){
        return zyjUserService.findZyjUsers();
    }
}
