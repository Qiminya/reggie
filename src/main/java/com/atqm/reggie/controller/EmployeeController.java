package com.atqm.reggie.controller;

import com.atqm.reggie.common.R;
import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(
            HttpServletRequest request,
            @RequestBody Employee employee){
        // 登录业务
        return employeeService.login(employee);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        return employeeService.logout();
    }

    @PostMapping("")
    public R<String> addEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping("/page")
    public R<Page<Employee>> getEmployeeForPage(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "name",required = false) String name
    ){

        return employeeService.getEmployeeForPage(page,pageSize,name);
    }

}
