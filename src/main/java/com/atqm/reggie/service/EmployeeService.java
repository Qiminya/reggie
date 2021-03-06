package com.atqm.reggie.service;

import com.atqm.reggie.common.R;
import com.atqm.reggie.entity.Employee;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EmployeeService extends IService<Employee> {
    public R<Employee> login(Employee employee);

    public R<String> logout();

    public R<String> saveEmployee(Employee employee);

    public R<Page<Employee>> getEmployeeForPage(Integer page, Integer pageSize, String input);

}
