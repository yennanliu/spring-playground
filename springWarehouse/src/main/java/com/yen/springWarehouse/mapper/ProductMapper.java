package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /** get info for pagination */
    // TODO : check @Param("ew") ??
    List<Product> getProductList(Page<Product> productPage, @Param("ew") Wrapper<Product> wrapper);

    @Update("update product set amount = amount - #{amount} where id = #{productId} and amount >= #{amount}")
    int updateProductCount(@Param("productId") Integer productId, @Param("amount") Integer amount);
}
