package com.xxy.p2p.controller;

import com.xxy.p2p.base.ErrorResponse;
import com.xxy.p2p.base.PageSet;
import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.constant.PageSetConstant;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.entity.dto.UserInfoDTO;
import com.xxy.p2p.service.TokenHelperService;
import com.xxy.p2p.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public abstract class BaseController {


    @Autowired
    TokenHelperService tokenHelperService;

    @Autowired
    UserService userService;

    public PageSet getPage(HttpServletRequest request) {
        // 返回数据的最后一条id
        String boundaryId = request.getParameter(PageSetConstant.BOUNDARY_ID);
        String pageNoStr = request.getParameter(PageSetConstant.PAGESET_PARAMETER);
        String sizeStr = request.getParameter(PageSetConstant.PAGESET_PAGESIZE);
        String start = request.getParameter(PageSetConstant.PAGESET_START);

        // 初始化
        PageSet pageSet = new PageSet(PageSetConstant.STATRT_INIT, PageSetConstant.PAGESIZE_TWENTY_FOUR);

        // 根据前端参数设置分页大小
        if (StringUtils.isNotBlank(sizeStr)) {
            int size = Integer.parseInt(sizeStr);
            // 限制小于等于100
            if (size <= 100) {
                pageSet.setPageSize(size);
            }
        }

        if (StringUtils.isNotBlank(start)) {
            pageSet.setStart(Integer.parseInt(start));
        }

        if (StringUtils.isNotBlank(boundaryId)) {
            pageSet.setBoundaryId(boundaryId);
        }

        if (StringUtils.isNotBlank(pageNoStr)) {
            int pageNo = Integer.parseInt(pageNoStr);
            pageSet.setCurrentPageNo(pageNo);
            pageSet.setStart((pageNo - 1) * pageSet.getPageSize());
        }

        return pageSet;
    }

    /**
     * 设置返回结果
     * @param t
     * @param <T>
     * @return
     */
    public <T> SuccessResponse<T> getSuccessResponse(T t) {
        return new SuccessResponse<>(t);
    }

    /**
     * 返回错误结果
     * @param errorCodeEnum
     * @return
     */
    public ErrorResponse getErrorResponse(ErrorCodeEnum errorCodeEnum) {
        return new ErrorResponse(errorCodeEnum);
    }

    protected UserInfoDO getUserInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        Integer userId =  tokenHelperService.get(token);
        List<UserInfoDO> userInfoDOS = userService.getByIdList(Arrays.asList(userId));
        if(!CollectionUtils.isEmpty(userInfoDOS)){
            return userInfoDOS.get(0);
        }
        return null;
    }
    protected UserInfoDTO getUserInfoDTO(HttpServletRequest request){
        String token = request.getHeader("token");
        Integer userId =  tokenHelperService.get(token);
        List<UserInfoDO> userInfoDOS = userService.getByIdList(Arrays.asList(userId));
        if(!CollectionUtils.isEmpty(userInfoDOS)){
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            BeanUtils.copyProperties(userInfoDOS.get(0), userInfoDTO);
            return userInfoDTO;
        }
        return null;
    }


}
