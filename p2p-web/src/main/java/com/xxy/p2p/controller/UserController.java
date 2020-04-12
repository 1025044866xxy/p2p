package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.entity.dto.UserInfoDTO;
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
    public SuccessResponse<UserInfoDTO> register(HttpServletRequest request){
        UserInfoDTO userInfoDTO = getUserInfoDTO(request);
        return getSuccessResponse(userInfoDTO);
    }

    @PostMapping("/update")
    public SuccessResponse<Boolean> update(String userName, Integer age, Integer gender,
                                           String phone, String mail, HttpServletRequest request){
        UserInfoDO userInfoDO = getUserInfo(request);
        return getSuccessResponse(userService.update(userInfoDO.getId(), userInfoDO));
    }

}
