package com.atguigu.scw.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.common.utils.RedisUtils;
import com.atguigu.scw.user.bean.TMemberAddress;
import com.atguigu.scw.user.bean.TMemberAddressExample;
import com.atguigu.scw.user.bean.TMemberExample;
import com.atguigu.scw.user.bean.TMemberExample.Criteria;
import com.atguigu.scw.user.exception.UserAccountException;
import com.atguigu.scw.user.mapper.TMemberAddressMapper;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.vo.request.UserRequestVO;
import com.atguigu.scw.user.vo.response.UserAddressVo;
import com.atguigu.scw.user.vo.response.UserResponseVO;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	TMemberMapper memberMapper;
	@Autowired
	TMemberAddressMapper memberAddressMapper;
	/**
	 * 注册的业务：
	 * 	1、登录账号必须唯一
	 * 	2、邮箱地址唯一
	 * 	3、密码加密
	 */
	@Override
	public void saveUser(UserRequestVO vo) {
		//将vo转为member： vo的属性名和member的一样
		TMember member = new TMember();
		BeanUtils.copyProperties(vo, member);
		TMemberExample e = new TMemberExample();
		//根据账号去统计数据库中同名的数量
		e.createCriteria().andLoginacctEqualTo(member.getLoginacct());
		//根据邮箱查询用户的数量
		Criteria c2 = e.createCriteria();
		c2.andEmailEqualTo(member.getEmail());
		e.or(c2);
		long countByExample = memberMapper.countByExample(e );
		if(countByExample>0) {
			//账号邮箱被占用
			throw new UserAccountException("账号或邮箱被占用!!");
		}
		//并给member设置默认值
		member.setAuthstatus("0");//未实名认证
		member.setUsertype("0");//用户类型  0个人    1企业
		member.setUsername(vo.getLoginacct());// 用户名不能为null，暂时使用手机号码作为用户名
		//密码加密: BCryptPasswordEncoder;
		member.setUserpswd(passwordEncoder.encode(member.getUserpswd()));
		
		memberMapper.insertSelective(member);
	}
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Override
	public AppResonse<Object> getUser(String loginacct, String userpswd) {
		TMemberExample e = new TMemberExample();
		//1、根据账号密码查询用户
		//将密码加密进行对比
		e.createCriteria().andLoginacctEqualTo(loginacct); //只根据账号查询用户信息
		List<TMember> list = memberMapper.selectByExample(e );
		//2、如果查询到登录成功
		if(CollectionUtils.isEmpty(list) || list.size()>1){
			return AppResonse.fail("登录失败，账号错误");
		}
		//3、将查询到的用户存到redis中，key使用唯一的字符串
		TMember member = list.get(0);
		//判断查询到用户的密码和传入的字符串是否一致
		boolean b = passwordEncoder.matches(userpswd, member.getUserpswd());//将浏览器传入的登录密码 和数据库查询到的加密后的密码进行比较
		if(!b) {
			return AppResonse.fail("登录失败，密码错误");
		}
		String token = RedisUtils.saveBean2Redis(member, stringRedisTemplate);
		//响应数据和其他已经封装的bean略有不同，可以重新封装为reponseVO
		UserResponseVO vo = new UserResponseVO();
		BeanUtils.copyProperties(member, vo);
		vo.setToken(token);
		//远程被调用的服务中如果返回的对象 ， 消费者没有使用对象的类型接收，那么会被自动转为map
		return AppResonse.ok(vo);
	}
	@Override
	public List<UserAddressVo> getUserAddresses(String accessToken) {
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, accessToken, TMember.class);
		if(member==null) {
			return null;
		}
		Integer id = member.getId();
		TMemberAddressExample example = new TMemberAddressExample();
		example.createCriteria().andMemberidEqualTo(id);
		List<TMemberAddress> list = memberAddressMapper.selectByExample(example);
		List<UserAddressVo> addressVoList = new ArrayList<UserAddressVo>();
		for (TMemberAddress tMemberAddress : list) {
			UserAddressVo vo = new UserAddressVo();
			BeanUtils.copyProperties(tMemberAddress, vo);
			addressVoList.add(vo);
		}
		
		return addressVoList;
	}
	@Override
	public TMember getMemberById(Integer memberid) {
		return memberMapper.selectByPrimaryKey(memberid);
	}

}
