package cn.my.chat.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.my.chat.model.User;

public interface UserDao {

	@Insert("insert into users (name,password) values (#{name},#{password}) ")
	int save(User user);

	@Select("select count(1) from users where name=#{name} and password=#{password}")
	int auth(@Param("name") String name, @Param("password") String password);

	@Select("select * from users where id=#{id}")
	User findById(Long id);
}
