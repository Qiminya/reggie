package com.atqm.reggie;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.service.EmployeeService;
import com.atqm.reggie.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.atqm.reggie.util.Constant.LOGIN_USER_KEY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReggieApplicationTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EmployeeService employeeService;

    @Test
    public void testRedis(){

        Employee employee = employeeService.getById(1);
        redisTemplate.opsForValue().set("test",JSON.toJSONString(employee));
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY, JSON.toJSONString(employee));
        Map<String, Object> employeeMap = BeanUtil.beanToMap(employee,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));
        System.out.println(employeeMap);
        //stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY,employeeMap);
        String name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("查到name的数据为："+name);
    }

    @Test
    public void addEmployee(){

        for (int i =0; i < 100 ;i++){
            Employee employee = new Employee();
            employee.setUsername("aaa"+i);
            employee.setName("牛马"+i);
            employee.setIdNumber("123333333333333333");
            employee.setPhone("18711711692");
            employee.setSex("0");
            employeeService.saveEmployee(employee);
        }
    }
}