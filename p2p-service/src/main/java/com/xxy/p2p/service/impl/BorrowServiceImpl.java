package com.xxy.p2p.service.impl;

import com.xxy.p2p.BaseService;
import com.xxy.p2p.dao.mapper.BorrowDAO;
import com.xxy.p2p.dao.mapper.RepaymentDAO;
import com.xxy.p2p.entity.domain.BorrowDO;
import com.xxy.p2p.entity.domain.RepaymentDO;
import com.xxy.p2p.entity.example.BorrowExample;
import com.xxy.p2p.entity.request.BorrowRequest;
import com.xxy.p2p.service.BorrowService;
import com.xxy.p2p.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;

@Service
public class BorrowServiceImpl extends BaseService implements BorrowService {

    @Resource
    BorrowDAO borrowDAO;

    @Resource
    RepaymentDAO repaymentDAO;

    @Transactional
    @Override
    public Boolean borrowMoney(BorrowRequest borrowRequest) throws ParseException {
        BorrowDO borrowDO = new BorrowDO();
        BeanUtils.copyProperties(borrowRequest, borrowDO);
        long dayCount = DateUtil.daysBetweenCount(borrowRequest.getStartDate(), borrowRequest.getEndDate());
        BigDecimal money = borrowDO.getMoney();
        BigDecimal interestMoney = money.multiply(BigDecimal.valueOf(dayCount)).multiply(borrowDO.getInterest());
        BigDecimal totalMoney = money.add(interestMoney);
        borrowDO.setInterestMoney(interestMoney);
        borrowDO.setTotalMoney(totalMoney);
        return borrowDAO.insert(borrowDO) > 0;
    }

    @Transactional
    @Override
    public Boolean repayment(BorrowRequest borrowRequest) {
        BorrowDO update = new BorrowDO();
        BeanUtils.copyProperties(borrowRequest, update);
        BigDecimal money = update.getMoney();
        BorrowExample example = new BorrowExample();
        example.setId(borrowRequest.getId());
        BorrowDO borrowDO = borrowDAO.getByExample(example);
        if(money.compareTo(borrowDO.getTotalMoney().subtract(borrowDO.getRepayment()))>=0){
            update.setState(1);
            update.setFinishDate(DateUtil.getNowDay());
        }
        update.setRepayment(borrowDO.getRepayment().add(money));
        update.setId(borrowDO.getId());
        RepaymentDO repaymentDO = new RepaymentDO();
        repaymentDO.setUserId(borrowDO.getUserId());
        repaymentDO.setMoney(money);
        repaymentDAO.insert(repaymentDO);
        return borrowDAO.update(update) > 0;
    }
}
