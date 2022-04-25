package com.atqm.reggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.atqm.reggie.common.R;
import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.mapper.EmployeeMapper;
import com.atqm.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.atqm.reggie.util.Constant.LOGIN_USER_KEY;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<Employee> login(Employee employee) {
        // 1.将用户输入的密码，进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee emp = this.getOne(queryWrapper);
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
        // 5.登录成功,将用户信息存入redis中30分钟有效期
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY, JSON.toJSONString(emp), 30, TimeUnit.MINUTES);

        return R.success(emp);
    }

    @Override
    public R<String> logout() {
        // 删除redis中的当前用户
        stringRedisTemplate.delete(LOGIN_USER_KEY);
        return R.success("退出登录成功！");
    }

    @Override
    public R<String> saveEmployee(Employee employee) {
        // 获取当前登录员工的id
        String employeeJSON = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY);
        Employee currentEmp = JSON.parseObject(employeeJSON, Employee.class);
        Long id = currentEmp.getId();

        // 设置输入员工的密码为123456加密后的密文
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 设置创建时间
        employee.setCreateTime(LocalDateTime.now());
        // 最后修改时间
        employee.setUpdateTime(LocalDateTime.now());
        // 创建人id
        employee.setCreateUser(id);
        // 最后修改人id
        employee.setUpdateUser(id);
        // 新增员工到数据库
        this.save(employee);

        return R.success("新增员工成功！");
    }
}
