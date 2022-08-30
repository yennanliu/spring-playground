package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=dNLGsANJ790&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=VqjaBphBdH4&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=t0pYgJu_nJ0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=52

import com.yen.springMybatisDemo1.bean.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DynamicSqlMapper {

    /** multi conditions select */
    List<Emp> getEmpByCondition(Emp emp);

    /** multi conditions select V2 */
    List<Emp> getEmpByCondition2(Emp emp);

    /** multi conditions select V3 */
    List<Emp> getEmpByCondition3(Emp emp);

}
