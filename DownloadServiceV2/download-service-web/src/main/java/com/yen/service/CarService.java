package com.yen.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.bean.Car;
import com.yen.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    CarMapper carMapper;

    public Car[] getAllCar(){
        return carMapper.getAllCar();
    }

    public Page getAllCarPage(int pageNum, int pageSize){
        //Pager<Car> pager = new Pager<Car>();
        //Page page = PageHelper.startPage(pageNum, pageSize);
        // List<SysUser> sysMenus = sysUserMapper.selectPage();

        System.out.println(">>> pageNum = " + pageNum);
        System.out.println(">>> pageSize = " + pageSize);

        PageHelper.startPage(pageNum, pageSize);
        List<Car> cars = carMapper.getAllCarByPage();
        Page page = (Page) cars;

        System.out.println(">>> page = " + page);
        System.out.println(">>> page.getResult() = " + page.getResult());

        return page;
    }


    public Car getCarByBrand(String brand){
        return carMapper.getCarByBrand(brand);
    }

    public void addNewCar(Car car){
        carMapper.insertCar(car);
    }

}
