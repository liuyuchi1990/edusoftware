package io.renren.modules.activity.controller;

import io.renren.modules.activity.entity.ActivityEntity;
import io.renren.modules.activity.service.ActivityService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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

    /**
     * 更新活动数据
     */
    @RequestMapping(value = "/updateActivityState", method = RequestMethod.POST)
    public ReturnResult updateActivityState(@RequestBody ActivityEntity activityEntity) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        if(activityEntity.getLikeNum()!=0){
          Map<String,Object> mp = activityService.queryLikeByUserIdAndActivityId(activityEntity);
          if(mp!=null){
              activityService.updateLikeState(activityEntity);
          }else{
              activityEntity.setTemplateId(UUID.randomUUID().toString().replaceAll("-", ""));
              activityService.insertLikeState(activityEntity);
          }
        }
        activityService.updateActivityState(activityEntity);
        result.setResult(map);
        return result;
    }


}
