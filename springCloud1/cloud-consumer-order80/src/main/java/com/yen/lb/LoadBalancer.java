package com.yen.lb;

// https://www.youtube.com/watch?v=m_MHwd3Dls4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=43

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {

    ServiceInstance instances(List<ServiceInstance> serviceInstanceList);
    
}
