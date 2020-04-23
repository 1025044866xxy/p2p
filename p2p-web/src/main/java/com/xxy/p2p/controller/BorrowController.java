package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.entity.dto.UserBorrowDTO;
import com.xxy.p2p.entity.dto.UserBorrowTotalDTO;
import com.xxy.p2p.entity.request.BorrowRequest;
import com.xxy.p2p.service.BorrowService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.ParseException;

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
        Integer userId = getUserId(request);
        borrowRequest.setUserId(userId);
        return getSuccessResponse(borrowService.borrowMoney(borrowRequest));
    }

    @PostMapping("/repayment")
    public SuccessResponse<Boolean> repayment(@RequestBody BorrowRequest borrowRequest,
                                              HttpServletRequest request) throws Exception{
        Integer userId = getUserId(request);
        borrowRequest.setUserId(userId);
        return getSuccessResponse(borrowService.repayment(borrowRequest));
    }

    @GetMapping("/total-info")
    public SuccessResponse<UserBorrowTotalDTO> totalInfo(HttpServletRequest request) throws ParseException {
        Integer userId = getUserId(request);
        return getSuccessResponse(borrowService.getUserTotalInfo(userId));
    }

    @GetMapping("/borrow-money/list")
    public SuccessResponse borrowMoneyList(HttpServletRequest request, BorrowRequest borrowRequest){
        Integer userId = getUserId(request);
        borrowRequest.setUserId(userId);
        return getSuccessResponse(borrowService.getBorrowMoneyList(borrowRequest, getPage(request)));
    }

    @GetMapping("/repayment/list")
    public SuccessResponse repaymentList(HttpServletRequest request, @NotNull Integer borrowId){
        Integer userId = getUserId(request);
        return getSuccessResponse(borrowService.getRepaymentList(userId, borrowId, getPage(request)));
    }

}
