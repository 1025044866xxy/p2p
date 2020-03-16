package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.entity.domain.UserDO;
import com.xxy.p2p.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    UserService userService;

    @GetMapping("/register")
    public SuccessResponse<Boolean> register(@NotBlank String accountNumber, @NotBlank String password){
        UserDO userDO = userService.getByAccountNumber(accountNumber);
        return getSuccessResponse(true);
    }
}
