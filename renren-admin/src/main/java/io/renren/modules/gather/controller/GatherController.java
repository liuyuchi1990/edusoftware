package io.renren.modules.gather.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.activity.entity.ActivityEntity;
import io.renren.modules.activity.service.ActivityService;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.gather.service.GatherService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-11 10:39:02
 */
@RestController
@RequestMapping("/gather")
public class GatherController {
    @Autowired
    private GatherService gatherService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    DistributionService distributionService;

    @Autowired
    private ActivityService activityService;


    @Value("${qr.gather}")
    String qrGatherUrl;
    @Value("${qr.gatherImgPath}")
    String qrGatherImgUrl;
    @Value("${qr.httpgatherurl}")
    String httpgatherurl;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = gatherService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ReturnResult info(@RequestBody GatherEntity gather) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        ActivityEntity act = new ActivityEntity();
        GatherEntity gatherEntity = gatherService.selectById(gather.getId());
        if("info".equals(gather.getType())){
            act.setId(gather.getId());
            act.setViewNum(1);
            activityService.updateActivityState(act);
        }
        map.put("gather",gatherEntity);
        result.setResult(map);
        return result;
    }

    /**
     * 列表
     */
    @RequestMapping("/queryAll")
    //@RequiresPermissions("sys:distribution:list")
    public ReturnResult queryAll(@RequestBody GatherEntity params) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        List<Map<String, Object>> activityLst = gatherService.queryList(params.getCreateUser());
        Map<String, Object> map = new HashedMap();
        map.put("data", activityLst);
        result.setResult(map);
        return result;
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ReturnResult save(@RequestBody GatherEntity gather) throws Exception {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        ActivityEntity act = new ActivityEntity();
        if ("".equals(gather.getId()) || gather.getId() == null) {
            gather.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            gather.setQrImg(httpgatherurl + gather.getId() + ".jpg");
            gather.setCreateTime(new Date());
            gather.setPrizeLeft(gather.getPriceNum());
            gather.setTemplateId(gather.getId());
            gatherService.insertAllColumn(gather);
            distributionService.insertActivity(gather);
            if((!"".equals(gather.getTemplateId())) && gather.getTemplateId() != null){
                act.setId(gather.getTemplateId());
                act.setUseNum(1);
                activityService.updateActivityState(act);
            }
            String text = qrGatherUrl.replace("id=", "id=" + gather.getId());
            QRCodeUtils.encode(text, null, qrGatherImgUrl, gather.getId(), true);
        } else {
            gather.setUpdateTime(new Date());
            gatherService.updateById(gather);//全部更新
            distributionService.updateActivity(gather);
        }
        map.put("gather",gather);
        result.setResult(map);
        return result;
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public ReturnResult copy(@RequestBody GatherEntity gather) throws Exception {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        GatherEntity ga = gatherService.selectById(gather.getId());
        Map<String, Object> map = new HashedMap();
        ActivityEntity act = new ActivityEntity();
        ga.setQrImg(httpgatherurl + ga.getId() + ".jpg");
        ga.setCreateTime(new Date());
        ga.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        gatherService.insertAllColumn(ga);
        distributionService.insertActivity(ga);
        if((!"".equals(gather.getTemplateId())) && gather.getTemplateId() != null){
            act.setId(gather.getTemplateId());
            act.setUseNum(1);
            activityService.updateActivityState(act);
        }
        String text = qrGatherUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrGatherImgUrl, ga.getId(), true);
        map.put("gather",ga);
        result.setResult(map);
        return result;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GatherEntity gather) {
        ValidatorUtils.validateEntity(gather);
        gatherService.updateById(gather);//全部更新
        return R.ok();
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult like(@RequestBody PrizeEntity pz) throws ParseException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, Object> pList = gatherService.queryLikeTime(pz);
        GatherEntity gz = gatherService.selectById(pz.getActivityId());
        long minutes = 0;
        Long prize_time = Long.parseLong(pList.get("prize_time").toString());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime toDate = LocalDateTime.now();
        String create_time = pList.get("create_time") == null ? null : pList.get("create_time").toString().replace(".0", "");
        if (pList.get("create_time") != null) {
            LocalDateTime ldt = LocalDateTime.parse(create_time, df);
            minutes = ChronoUnit.MINUTES.between(ldt, toDate);
        }

        if (create_time == null || gz.getRestrictTime() < (minutes/60)) {
            pz.setUpdateTime(new Date());
            gatherService.insertLikeLog(pz);
            Map<String, Object> p = gatherService.queryPrizeLog(pz.getId());
            int arr = p.get("likes") == null ? 0 : p.get("likes").toString().split(",").length;
            if (arr == (gz.getTargetNum()-1)) {
                pz.setCompleteTime(new Date());
                gatherService.releasePrize(pz.getActivityId());
            }
            int mp = gatherService.updatePrizeLog(pz);
            map.put("data", mp);
        } else {
            result.setCode(ReturnCodeEnum.INVOKE_VENDOR_DF_ERROR.getCode());
            result.setMsg("请您休息" + (gz.getRestrictTime() - minutes/60) + "小时"+ (gz.getRestrictTime()*60-minutes)%60 +"分钟后再次砍价");
        }
        map.put("status", "success");
        map.put("msg", "send ok");

        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/makeLike", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult makeLike(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        SysUserEntity user = new SysUserEntity();
        Map<String, Object> map = new HashedMap();
        pz.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        int mp = gatherService.insertPrizeLog(pz);
        user.setUsername(pz.getUserName());
        user.setUserId(pz.getUserId());
        user.setMobile(pz.getMobile());
        sysUserService.updateUser(user);
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", pz);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/queryLike", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryLike(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> mp = gatherService.queryLike(pz.getActivityId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/queryLikeLog", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryLikeLog(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> mp = gatherService.queryLikeLog(pz.getId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/queryPrizeLog", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryPrizeLog(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, Object> mp = gatherService.queryPrizeLog(pz.getId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Transactional
    public ReturnResult delete(@RequestBody String[] ids) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        gatherService.deleteBatchIds(Arrays.asList(ids));
        distributionService.deleteActivity(Arrays.asList(ids));
        return result;
    }

}
