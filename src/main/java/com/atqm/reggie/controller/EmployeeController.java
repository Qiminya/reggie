package com.atqm.reggie.controller;

import com.atqm.reggie.common.R;
import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
         // 1.将用户输入的密码，进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null){
            // 2.1用户名不存在
            return R.error("用户不存在，登录失败！");
        }
        // 3.用户存在，查询密码是否匹配
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误，登录失败！");
        }
        // 4.账号是否禁用
        if (emp.getStatus() == 0){
            return R.error("账号已禁用！");
        }
        // 5.登录成功,将用户信息存入session中
        request.getSession().setAttribute("employee", emp);
        return R.success(emp);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 删除cookie中的当前登录的员工信息
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功！");
    }
}
