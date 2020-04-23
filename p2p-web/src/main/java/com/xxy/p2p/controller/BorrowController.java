package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.entity.request.BorrowRequest;
import com.xxy.p2p.service.BorrowService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/borrow")
public class BorrowController extends BaseController {

    @Resource
    BorrowService borrowService;


    @PostMapping("/borrow-money")
    public SuccessResponse<Boolean> borrowMoney(@RequestBody BorrowRequest borrowRequest,
                                                HttpServletRequest request) throws Exception {
        Assert.isTrue(borrowRequest.getStartDate() != null &&
                borrowRequest.getEndDate() != null && borrowRequest.getInterest() != null
                , ErrorCodeEnum.P01.getCode());
        Integer userId = getUserInfo(request).getId();
        borrowRequest.setUserId(userId);
        return getSuccessResponse(borrowService.borrowMoney(borrowRequest));
    }

    @PostMapping("/repayment")
    public SuccessResponse<Boolean> repayment(@RequestBody BorrowRequest borrowRequest,
                                              HttpServletRequest request) throws Exception{
        Integer userId = getUserInfo(request).getId();
        borrowRequest.setUserId(userId);
        return getSuccessResponse(borrowService.repayment(borrowRequest));
    }

}
