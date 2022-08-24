package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=D_DDE_o7ks0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=gk_pm_Uaa_Y&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=42

import com.yen.springMybatisDemo1.bean.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpMapper {

    /** get all employees */
    List<Emp> getAllEmp();

    /** get all employees V2 */
    List<Emp> getAllEmp2();
}
