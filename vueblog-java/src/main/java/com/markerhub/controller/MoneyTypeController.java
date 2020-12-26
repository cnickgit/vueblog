package com.markerhub.controller;

import com.markerhub.common.lang.Result;
import com.markerhub.service.MoneyTypeService;
import com.markerhub.vo.AddMoneyTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MoneyTypeController {

    @Autowired
    private MoneyTypeService moneyTypeService;

    @PostMapping("/addMoneyType")
    public Result addMoneyType(@RequestBody  AddMoneyTypeVo addMoneyTypeVo){
        if(null == addMoneyTypeVo){
            return Result.fail("对象为空!");
        }
        return moneyTypeService.addMoneyType(addMoneyTypeVo);
    }

    @GetMapping("/moneyTypes")
    public Result getMoneyType(){
        return moneyTypeService.getMoneyType();
    }

    @GetMapping("/moneyType/{id}")
    public Result getMoneyTypeById(@PathVariable("id") String id){
        return moneyTypeService.getMoneyTypeById(id);
    }
}
