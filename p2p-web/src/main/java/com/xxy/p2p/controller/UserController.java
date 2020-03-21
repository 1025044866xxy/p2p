package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.entity.domain.UserDO;
import com.xxy.p2p.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    UserService userService;

    @GetMapping("/info")
    public SuccessResponse<UserDO> register(HttpServletRequest request){
        UserDO userDO = getUserInfo(request);
        return getSuccessResponse(userDO);
    }

    @PostMapping("/update")
    public SuccessResponse<Boolean> update(String userName, Integer age, Integer gender,
                                           String phone, String mail, HttpServletRequest request){
        UserDO userDO = getUserInfo(request);
        UserDO update = new UserDO();
        update.setAge(age);
        update.setMail(mail);
        update.setGender(gender);
        update.setUserName(userName);
        update.setPhone(phone);
        return getSuccessResponse(userService.update(userDO.getId(), update));
    }

}
