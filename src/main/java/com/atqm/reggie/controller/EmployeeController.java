package com.atqm.reggie.controller;

import com.alibaba.fastjson.JSON;
import com.atqm.reggie.common.R;
import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.service.EmployeeService;
import com.atqm.reggie.util.BaseContent;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static com.atqm.reggie.util.Constant.LOGIN_USER_KEY;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @GetMapping("/{id}")
    public R<Employee> queryEmployeeById(@PathVariable("id") Long id){
        // 封装查询条件
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        // 根据id查询员工
        Employee employee = employeeService.getOne(queryWrapper);
        System.out.println(employee);

        return R.success(employee);
    }

    @PutMapping("")
    public R<String> editEmployee(@RequestBody Employee employee){
        // 获取当前登录员工的id
        String employeeJSON = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY);
        Employee currentEmp = JSON.parseObject(employeeJSON, Employee.class);
        Long id = currentEmp.getId();
        BaseContent.setCurrentId(id);
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return R.success("员工"+employee.getName()+"修改成功");
    }
}
