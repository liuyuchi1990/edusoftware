/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.order.model.Order;
import io.renren.modules.sys.entity.Approval;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(String userId);

	SysUserEntity queryById(String id);

	SysUserEntity queryByUserName(SysUserEntity user);

	SysUserEntity queryByOpenId(String id);

	SysUserEntity queryByAppOpenId(String id);

	SysUserEntity queryByMobile(String mobile);

	void insertUser (SysUserEntity user);

	void updateUser(SysUserEntity user);

	void releaseAccount(SysUserEntity user);

	void insertApproval(Approval approval);

	void updateAccount(Order order);

	void updateApproval(Approval approval);

	List<SysUserEntity>  queryForUsers(String[] ids);

	List<SysUserEntity>  queryAllUsers(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    List<Approval>  queryAllApproval(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,@Param("userId") String userId);
}
