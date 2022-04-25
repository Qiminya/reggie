package com.atqm.reggie.handler;

import com.atqm.reggie.entity.Employee;
import com.atqm.reggie.util.BaseContent;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入时填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContent.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContent.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("修改时填充");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContent.getCurrentId());
    }
}
