package com.yen.SpringDistributionLock.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yen.SpringDistributionLock.mapper.StockMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class StockServiceWithOptimisticLock { // default : Singleton (@Scope("Singleton"))

    @Autowired
    StockMapper stockMapper;

    private ReentrantLock lock = new ReentrantLock();

    @Transactional
    public void deduct() {

        // step 1) get storage count and lock record
        List<Stock> stockList = stockMapper.selectList(new QueryWrapper<Stock>().eq("product_code", "prod-1"));
        // get 1st product (stock) for following op
        Stock stock = stockList.get(0);

        // step 2) check if count is enough (>0)
        if (stock != null && stock.getCount() > 0){

            // step 3) deduct storage
            stock.setCount(stock.getCount()-1);

            /** update version !!! */
            Integer version = stock.getVersion();
            stock.setVersion(version+1);

            //stockMapper.update(stock, new UpdateWrapper<Stock>().eq("id", stock.getId()).eq("version", version));
            if (stockMapper.update(stock, new UpdateWrapper<Stock>().eq("id", stock.getId()).eq("version", version)) == 0){
                // if result == 0, means update FAIL, retry (recursion call)
                System.out.println(">>> update failed, retry ...");
                this.deduct();
            }

        }
    }

}
