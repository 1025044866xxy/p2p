package com.xxy.p2p.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xxy.p2p.BaseService;
import com.xxy.p2p.base.PageSet;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.dao.mapper.BorrowDAO;
import com.xxy.p2p.dao.mapper.RepaymentDAO;
import com.xxy.p2p.entity.domain.BorrowDO;
import com.xxy.p2p.entity.domain.RepaymentDO;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.entity.dto.UserBorrowDTO;
import com.xxy.p2p.entity.dto.UserBorrowTotalDTO;
import com.xxy.p2p.entity.example.BorrowExample;
import com.xxy.p2p.entity.example.RepaymentExample;
import com.xxy.p2p.entity.request.BorrowRequest;
import com.xxy.p2p.service.BorrowService;
import com.xxy.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl extends BaseService implements BorrowService {

    private static Logger logger = LoggerFactory.getLogger(BorrowServiceImpl.class);

    @Resource
    BorrowDAO borrowDAO;

    @Resource
    RepaymentDAO repaymentDAO;

    static Lock lock = new ReentrantLock();

    static Map<Integer, Integer> dateMap = new HashMap<>();
    static Map<Integer, Integer> interestMap = new HashMap<>();

    static {
        dateMap.put(1,7);
        dateMap.put(2,30);
        dateMap.put(3,90);
        dateMap.put(4,180);
        dateMap.put(5,365);
        interestMap.put(1, 31);
        interestMap.put(2, 29);
        interestMap.put(3, 25);
        interestMap.put(4, 21);
        interestMap.put(5, 18);
    }


    @Transactional
    @Override
    public Boolean borrowMoney(BorrowRequest borrowRequest) throws ParseException {
        Integer flag = 0;
        Integer type = borrowRequest.getType();
        UserInfoDO userInfoDO =getUserById(borrowRequest.getUserId());
        StringBuilder url = new StringBuilder("PhoneCertification=1&HouseholdCertification=1");
        url.append("&Age=").append(userInfoDO.getAge()).append("&Sex=").append(userInfoDO.getGender());
        UserBorrowTotalDTO userBorrowTotalDTO = getUserTotalInfo(borrowRequest.getUserId());
        url.append("&LoanAmount=").append(borrowRequest.getMoney()).append("&LoanPeriod=").append(dateMap.get(type));
        url.append("&LoanRate=").append(interestMap.get(type)).append("&HistoryBorrowingTimes=").append(userBorrowTotalDTO.getTotalCount());
        if(userBorrowTotalDTO.getTotalCount() == 0){
            url.append("&IsFirstBid=1");
        }else{
            url.append("&IsFirstBid=0");
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL("http://y31402x356.qicp.vip/user/getResult");
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(url);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            flag = 0;
            logger.info("调用资质审核接口有误!"+e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        if(StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.parseObject(result);
            flag = (Integer) jsonObject.get("result");
        }
        Assert.isTrue(flag == 1, ErrorCodeEnum.Q01.getCode());
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
        Boolean result = Boolean.FALSE;
        BorrowDO update = new BorrowDO();
        BeanUtils.copyProperties(borrowRequest, update);
        BigDecimal money = update.getMoney();
        BorrowExample example = new BorrowExample();
        example.setId(borrowRequest.getId());
        BorrowDO borrowDO = borrowDAO.getByExample(example);
        Assert.isTrue(borrowDO.getState() == 0 , ErrorCodeEnum.Q02.getCode());
        try {
            lock.lock();
            borrowDO = borrowDAO.getByExample(example);
            Assert.isTrue(borrowDO.getState() == 0 , ErrorCodeEnum.Q02.getCode());
            money = money.add(borrowDO.getRepayment());
            BigDecimal totalMoney = getTotalMoney(borrowDO);
            Assert.isTrue(money.compareTo(totalMoney)<=0, ErrorCodeEnum.Q03.getCode());
            if(money.compareTo(totalMoney) == 0){
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
            result = borrowDAO.update(update) > 0;
        }finally {
            lock.unlock();
        }
        return result;
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
