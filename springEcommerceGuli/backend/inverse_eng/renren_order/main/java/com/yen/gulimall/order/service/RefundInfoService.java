package com.yen.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
//import com.yen.common.utils.PageUtils;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:12:52
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

