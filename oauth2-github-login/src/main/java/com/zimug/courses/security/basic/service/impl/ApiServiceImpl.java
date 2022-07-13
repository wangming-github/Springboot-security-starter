package com.zimug.courses.security.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zimug.courses.security.basic.mapper.ApiMapper;
import com.zimug.courses.security.basic.pojo.Api;
import com.zimug.courses.security.basic.service.ApiService;
import org.springframework.stereotype.Service;

/**
* @author maizi
* @description 针对表【sys_api(系统接口表)】的数据库操作Service实现
* @createDate 2022-07-07 03:31:46
*/
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api>
    implements ApiService{

}




