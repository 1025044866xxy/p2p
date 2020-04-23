package com.xxy.p2p.service;

import com.xxy.p2p.entity.dto.UserBorrowTotalDTO;
import com.xxy.p2p.entity.request.BorrowRequest;

import java.text.ParseException;

public interface BorrowService {
    Boolean borrowMoney(BorrowRequest borrowRequest) throws Exception;

    Boolean repayment(BorrowRequest borrowRequest) throws Exception;

    UserBorrowTotalDTO getUserTotalInfo(Integer userId) throws ParseException;
}
