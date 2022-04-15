package com.atqm.reggie.service.impl;

import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.mapper.EmployeeMapper;
import com.atqm.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
