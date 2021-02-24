package com.markerhub.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.User;
import com.markerhub.entity.ZyjUser;
import com.markerhub.mapper.LoginUserMapper;
import com.markerhub.mapper.UserMapper;
import com.markerhub.service.UserService;
import com.markerhub.service.ZyjUserService;
import com.markerhub.util.TokenUtil;
import com.markerhub.vo.LoginUser;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    LoginUserMapper loginUserMapper;

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

    @GetMapping("/zyjUsersPort/{port}")
    public Result findUsersByPort(@PathVariable("port") String port){
        return zyjUserService.findZyjUsersByPort(port);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> para) throws JsonProcessingException {
        String username=(String)para.get("username");
        String password=(String)para.get("password");
        //检验用户名密码是否正确
        int count = 0;
        count = loginUserMapper.selectUser(username, password);
        if(count > 0){
            String token= TokenUtil.sign(new LoginUser(username,password));
            HashMap<String,Object> hs=new HashMap<>();
            hs.put("token",token);
//            ObjectMapper objectMapper=new ObjectMapper();
            return Result.succ(hs);
        }else {
            String msg = "登录失败,用户不存在";
//            ObjectMapper objectMapper=new ObjectMapper();
            return Result.fail(msg);
        }
    }
}
