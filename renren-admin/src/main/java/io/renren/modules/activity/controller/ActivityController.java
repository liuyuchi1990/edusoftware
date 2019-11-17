package io.renren.modules.activity.controller;

import io.renren.modules.activity.entity.ActivityEntity;
import io.renren.modules.activity.service.ActivityService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-25 15:35:24
 */
@RestController
@RequestMapping("api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    /**
     * 列表
     */
    @RequestMapping(value = "/queryAllTemplate", method = RequestMethod.GET)
    public ReturnResult queryAllTemplate(@RequestParam Map<String, Object> params) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        List<String> lst = new ArrayList();
        if(params.get("activityId")!=null){
            lst = Arrays.asList(params.get("activityId").toString().split(","));
        }
        List<ActivityEntity> activityEntityList = activityService.queryAllTemplate(params,lst);
        map.put("data", activityEntityList);
        result.setResult(map);
        return result;
    }
}
