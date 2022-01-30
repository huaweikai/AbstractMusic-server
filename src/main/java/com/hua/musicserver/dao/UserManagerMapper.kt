package com.hua.musicserver.dao

import com.hua.musicserver.bean.UserBean
import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository

@Mapper
interface UserManagerMapper {

    @Insert("insert into user (name,email,passwd,head) values(#{name},#{email},#{passwd},#{head})")
    fun insertUser(user: UserBean): Int

    @Select("select * from user where name = #{name}")
    fun selectUser(@Param("name") name: String): UserBean?

    @Select("select * from user where id = #{id}")
    fun selectUserById(@Param("id") id: Int): UserBean

    @Delete("delete from user where id = #{id}")
    fun deleteUser(@Param("id") id: Int): Int

    @Select("select count(*) from user where email = #{email}")
    fun selectEmailCount(@Param("email") email: String): Int

    @Select("select count(*) from user where name = #{name}")
    fun selectNameCount(@Param("name") name: String): Int

    @Select("select * from user where email = #{email}")
    fun selectUserByEmail(@Param("email") email: String): UserBean?

    @Select("select id from user where email = #{email}")
    fun selectIdByEmail(@Param("email") email: String): Int

    @Update("update user set name = #{name},email = #{email},passwd = #{passwd},head= #{head} where id = #{id}")
    fun updateUser(user: UserBean): Int
}