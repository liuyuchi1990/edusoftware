package io.renren.modules.distribution.service.impl;

import io.renren.common.config.Constants;
import io.renren.modules.bargin.entity.BarginEntity;
import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.lottery.entity.LotteryEntity;
import io.renren.modules.activity.entity.ActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

@Service("distributionService")
public class DistributionServiceImpl extends ServiceImpl<DistributionDao, Distribution> implements DistributionService, Serializable {
    @Autowired
    private DistributionDao distributionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<Distribution> page = this.selectPage(
                new Query<Distribution>(params).getPage(),
                new EntityWrapper<Distribution>()
        );

        return new PageUtils(page);
    }


    public List<Distribution> queryList(Map<String, Object> params) {
        return distributionDao.queryList(params);
    }

    public List<Distribution> queryListByPage(Map<String, Object> params) {
        return distributionDao.queryListByPage(params);
    }

    public List<ActivityEntity> queryActivity(Map<String, Object> params){
        return distributionDao.queryActivity(params);
    }

    public Distribution queryById(String id) {
        return distributionDao.queryById(id);
    }


    public int insertDistribution(Distribution distribution) {
        return distributionDao.insertDistribution(distribution);
    }

    public int insertActivity(Distribution distribution) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(distribution.getId());
        activityEntity.setActivityName(distribution.getActivityName());
        activityEntity.setActivityState("1");
        activityEntity.setQr(distribution.getQrImg());
        activityEntity.setThumbnail(distribution.getThumbnail());
        activityEntity.setEndTime(distribution.getEndTime());
        activityEntity.setStartTime(distribution.getStartTime());
        activityEntity.setActivityType(Constants.DISTRIBUTION);
        activityEntity.setCreateUser(distribution.getCreateUser());
        activityEntity.setTemplateId(distribution.getTemplateId());
        return distributionDao.insertActivity(activityEntity);
    }


    public int updateActivity(Distribution distribution) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(distribution.getId());
        activityEntity.setActivityName(distribution.getActivityName());
        activityEntity.setActivityState("1");
        activityEntity.setThumbnail(distribution.getThumbnail());
        activityEntity.setEndTime(distribution.getEndTime());
        activityEntity.setStartTime(distribution.getStartTime());
        activityEntity.setActivityType(Constants.DISTRIBUTION);
        return distributionDao.updateActivity(activityEntity);
    }


    public int insertActivity(GatherEntity gatherEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(gatherEntity.getId());
        activityEntity.setActivityName(gatherEntity.getActivityName());
        activityEntity.setThumbnail(gatherEntity.getThumbnail());
        activityEntity.setEndTime(gatherEntity.getEndTime());
        activityEntity.setQr(gatherEntity.getQrImg());
        activityEntity.setStartTime(gatherEntity.getStartTime());
        if(Constants.HELP.equals(gatherEntity.getType())){
            activityEntity.setActivityType(Constants.HELP);
        }else{
            activityEntity.setActivityType(Constants.GATHER);
        }
        activityEntity.setPrizeNum(gatherEntity.getPriceNum());
        activityEntity.setPrizeLeft(gatherEntity.getPriceNum());
        activityEntity.setCreateUser(gatherEntity.getCreateUser());
        activityEntity.setTemplateId(gatherEntity.getTemplateId());
        return distributionDao.insertActivity(activityEntity);
    }

    public int updateActivity(GatherEntity gatherEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(gatherEntity.getId());
        activityEntity.setActivityName(gatherEntity.getActivityName());

        activityEntity.setThumbnail(gatherEntity.getThumbnail());
        activityEntity.setEndTime(gatherEntity.getEndTime());
        activityEntity.setStartTime(gatherEntity.getStartTime());
        activityEntity.setActivityType(Constants.GATHER);
        return distributionDao.updateActivity(activityEntity);
    }


    public int insertActivity(BarginEntity barginEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(barginEntity.getId());
        activityEntity.setActivityName(barginEntity.getActivityName());
        activityEntity.setThumbnail(barginEntity.getThumbnail());
        activityEntity.setEndTime(barginEntity.getEndTime());
        activityEntity.setStartTime(barginEntity.getStartTime());
        activityEntity.setQr(barginEntity.getQrImg());
        activityEntity.setActivityType(Constants.BARGIN);
        activityEntity.setPrizeNum(barginEntity.getPrizeNum());
        activityEntity.setTemplateId(barginEntity.getTemplateId());
        activityEntity.setPrizeLeft(barginEntity.getPrizeNum());
        activityEntity.setCreateUser(barginEntity.getCreateUser());
        activityEntity.setTemplateId(barginEntity.getTemplateId());
        return distributionDao.insertActivity(activityEntity);
    }

    public int updateActivity(BarginEntity barginEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(barginEntity.getId());
        activityEntity.setActivityName(barginEntity.getActivityName());
        activityEntity.setThumbnail(barginEntity.getThumbnail());
        activityEntity.setEndTime(barginEntity.getEndTime());
        activityEntity.setStartTime(barginEntity.getStartTime());
        activityEntity.setActivityType(Constants.BARGIN);
        return distributionDao.updateActivity(activityEntity);
    }


    public int insertActivity(GrouponEntity grouponEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(grouponEntity.getId());
        activityEntity.setActivityName(grouponEntity.getActivityName());
        activityEntity.setThumbnail(grouponEntity.getThumbnail());
        activityEntity.setEndTime(grouponEntity.getEndTime());
        activityEntity.setStartTime(grouponEntity.getStartTime());
        activityEntity.setQr(grouponEntity.getQrImg());
        activityEntity.setActivityType(Constants.GROUPON);
        activityEntity.setCreateUser(grouponEntity.getCreateUser());
        activityEntity.setTemplateId(grouponEntity.getTemplateId());
        return distributionDao.insertActivity(activityEntity);
    }

    public int updateActivity(GrouponEntity grouponEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(grouponEntity.getId());
        activityEntity.setActivityName(grouponEntity.getActivityName());
        activityEntity.setThumbnail(grouponEntity.getThumbnail());
        activityEntity.setEndTime(grouponEntity.getEndTime());
        activityEntity.setStartTime(grouponEntity.getStartTime());
        activityEntity.setActivityType(Constants.GROUPON);
        return distributionDao.updateActivity(activityEntity);
    }

    public int insertActivity(LotteryEntity lotteryEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(lotteryEntity.getId());
        activityEntity.setActivityName(lotteryEntity.getActivityName());
        activityEntity.setThumbnail(lotteryEntity.getThumbnail());
        activityEntity.setActivityType(Constants.Lottery);
        activityEntity.setQr(lotteryEntity.getQrImg());
        activityEntity.setCreateUser(lotteryEntity.getCreateUser());
        activityEntity.setTemplateId(lotteryEntity.getTemplateId());
        return distributionDao.insertActivity(activityEntity);
    }

    public int updateActivity(LotteryEntity lotteryEntity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(lotteryEntity.getId());
        activityEntity.setActivityName(lotteryEntity.getActivityName());
        activityEntity.setThumbnail(lotteryEntity.getThumbnail());
        activityEntity.setActivityType(Constants.Lottery);
        return distributionDao.updateActivity(activityEntity);
    }

    public int deleteActivity(List<String> ids) {
        return distributionDao.deleteActivity(ids);
    }

    public int release(String id) {
        return distributionDao.release(id);
    }

    public int rollback(String id) {
        return distributionDao.rollback(id);
    }

    public int addWatcher(Distribution distribution) {
        return distributionDao.addWatcher(distribution);
    }
}
