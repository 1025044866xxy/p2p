package com.xxy.p2p.service.impl;

import com.xxy.p2p.BaseService;
import com.xxy.p2p.base.PageSet;
import com.xxy.p2p.dao.mapper.BorrowDAO;
import com.xxy.p2p.dao.mapper.RepaymentDAO;
import com.xxy.p2p.entity.domain.BorrowDO;
import com.xxy.p2p.entity.domain.RepaymentDO;
import com.xxy.p2p.entity.dto.UserBorrowDTO;
import com.xxy.p2p.entity.dto.UserBorrowTotalDTO;
import com.xxy.p2p.entity.example.BorrowExample;
import com.xxy.p2p.entity.example.RepaymentExample;
import com.xxy.p2p.entity.request.BorrowRequest;
import com.xxy.p2p.service.BorrowService;
import com.xxy.p2p.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl extends BaseService implements BorrowService {

    @Resource
    BorrowDAO borrowDAO;

    @Resource
    RepaymentDAO repaymentDAO;

    static Map<Integer, Integer> dateMap = new HashMap<>();

    static {
        dateMap.put(1,7);
        dateMap.put(2,30);
        dateMap.put(3,90);
        dateMap.put(4,180);
        dateMap.put(5,365);
    }

    @Transactional
    @Override
    public Boolean borrowMoney(BorrowRequest borrowRequest) throws ParseException {
        BorrowDO borrowDO = new BorrowDO();
        BeanUtils.copyProperties(borrowRequest, borrowDO);
        borrowDO.setStartDate(DateUtil.getNowDay());
        borrowDO.setEndDate(DateUtil.addDeltaDays(borrowDO.getStartDate(), dateMap.get(borrowRequest.getType())));
        BigDecimal money = borrowDO.getMoney();
        BigDecimal interestMoney = money.multiply(borrowDO.getInterest());
        borrowDO.setInterestMoney(interestMoney);
        borrowDO.setOverdueInterest(new BigDecimal("1.5").multiply(borrowDO.getInterest()));
        borrowDO.setOverdueInterestMoney(money.multiply(borrowDO.getOverdueInterest()));
        borrowDO.setOverdueMoney(money.multiply(new BigDecimal("0.1")));
        return borrowDAO.insert(borrowDO) > 0;
    }

    @Transactional
    @Override
    public Boolean repayment(BorrowRequest borrowRequest) throws Exception{
        BorrowDO update = new BorrowDO();
        BeanUtils.copyProperties(borrowRequest, update);
        BigDecimal money = update.getMoney();
        BorrowExample example = new BorrowExample();
        example.setId(borrowRequest.getId());
        BorrowDO borrowDO = borrowDAO.getByExample(example);
        money = money.add(borrowDO.getRepayment());
        BigDecimal totalMoney = getTotalMoney(borrowDO);
        if(money.compareTo(totalMoney) >= 0){
            update.setState(1);
            update.setFinishDate(DateUtil.getNowDay());
        }
        update.setRepayment(money);
        update.setId(borrowDO.getId());
        RepaymentDO repaymentDO = new RepaymentDO();
        repaymentDO.setUserId(borrowDO.getUserId());
        repaymentDO.setMoney(borrowRequest.getMoney());
        repaymentDO.setBorrowId(borrowRequest.getId());
        repaymentDAO.insert(repaymentDO);
        return borrowDAO.update(update) > 0;
    }

    @Override
    public UserBorrowTotalDTO getUserTotalInfo(Integer userId) throws ParseException {
        BigDecimal totalRepayment = BigDecimal.ZERO;
        BigDecimal totalBorrowMoney = BigDecimal.ZERO;
        BigDecimal totalMoney = BigDecimal.ZERO;
        Integer successTotal = 0;
        Integer failTotal = 0;
        BorrowExample example = new BorrowExample();
        example.setUserId(userId);
        List<BorrowDO> borrowDOS = borrowDAO.listByExample(example, null);
        for (BorrowDO borrowDO: borrowDOS){
            totalMoney = totalMoney.add(borrowDO.getMoney());
            totalRepayment = totalRepayment.add(borrowDO.getRepayment());
            if (borrowDO.getState() == 1){
                successTotal++;
            }else {
                if(DateUtil.timeAfter(DateUtil.getNowDay(), borrowDO.getEndDate())){
                    failTotal++;
                }
                totalBorrowMoney = totalBorrowMoney.add(getTotalMoney(borrowDO).subtract(borrowDO.getRepayment()));
            }
        }
        UserBorrowTotalDTO result = new UserBorrowTotalDTO();
        result.setTotalCount(borrowDOS.size());
        result.setFailTotal(failTotal);
        result.setSuccessTotal(successTotal);
        result.setTotalBorrowMoney(totalBorrowMoney);
        result.setTotalMoney(totalMoney);
        result.setTotalRepayment(totalRepayment);
        return result;
    }

    @Override
    public Object getBorrowMoneyList(BorrowRequest borrowRequest, PageSet pageSet) {
        BorrowExample example = new BorrowExample();
        BeanUtils.copyProperties(borrowRequest, example);
        if(Objects.equals(2,example.getState())){
            example.setFinishDate(DateUtil.getNowDay());
        }
        Integer count = borrowDAO.count(example);
        if(count == 0){
            return getEmptyPageSet(pageSet);
        }
        List<UserBorrowDTO> resultList = borrowDAO.listByExample(example, pageSet).stream().map(borrowDO -> {
            UserBorrowDTO userBorrowDTO = new UserBorrowDTO();
            BeanUtils.copyProperties(borrowDO, userBorrowDTO);
            try {
                userBorrowDTO.setTotalMoney(getTotalMoney(borrowDO));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return userBorrowDTO;
        }).collect(Collectors.toList());
        pageSet.setResultCount(count);
        pageSet.setResultList(resultList);
        return pageSet;
    }

    @Override
    public Object getRepaymentList(Integer userId, Integer borrowId, PageSet pageSet) {
        RepaymentExample example = new RepaymentExample();
        example.setUserId(userId);
        example.setBorrowId(borrowId);
        Integer count = repaymentDAO.count(example);
        if(count == 0){
            return getEmptyPageSet(pageSet);
        }
        List<RepaymentDO> repaymentDOS = repaymentDAO.listByExample(example, pageSet);
        pageSet.setResultCount(count);
        pageSet.setResultList(repaymentDOS);
        return pageSet;
    }

    private BigDecimal getTotalMoney(BorrowDO borrowDO) throws ParseException {
        BigDecimal totalMoney;
        String today = DateUtil.getNowDay();
        if(DateUtil.timeAfter(today, borrowDO.getEndDate())){
            totalMoney = borrowDO.getMoney().add(borrowDO.getInterestMoney().multiply(
                    BigDecimal.valueOf(DateUtil.daysBetweenCount(borrowDO.getStartDate(), borrowDO.getEndDate()))));
            totalMoney = totalMoney.add(borrowDO.getOverdueMoney());
            totalMoney = totalMoney.add(borrowDO.getOverdueInterestMoney().multiply(BigDecimal.valueOf(
                    DateUtil.daysBetweenCount(borrowDO.getEndDate(), today))));

        }else{
            totalMoney = borrowDO.getMoney().add(borrowDO.getInterestMoney().multiply(
                    BigDecimal.valueOf(DateUtil.daysBetweenCount(borrowDO.getStartDate(), today))));
        }
        return totalMoney;
    }
}
