package com.yen.coupon.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.common.utils.PageUtils;
import com.yen.coupon.coupon.entity.SeckillPromotionEntity;

import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-03 09:12:35
 */
public interface SeckillPromotionService extends IService<SeckillPromotionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

