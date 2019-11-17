package io.renren.modules.activity.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.activity.entity.ActivityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-25 15:35:24
 */
public interface ActivityDao extends BaseMapper<ActivityEntity> {

    List<Map<String, Object>> queryBarginLog(String id);

    List<Map<String, Object>> queryList(String id);

    List<ActivityEntity> queryAllTemplate(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("list") List<String> list);
}
