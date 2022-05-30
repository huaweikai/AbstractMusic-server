# MusicServer

#### 介绍
抽象音乐app后端，用于抽象音乐毕设服务端，包含用户登录与注册，在线媒体检索，专辑列表等等功能。

#### 软件架构
软件架构说明


#### 数据库说明
用户表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	用户ID（自增）  
name	varchar(20)		否	无	用户名  
passwd	varchar(20)		否	无	用户密码  
email	varchar(20)	唯一性	否	无	用户邮箱  
head	varchar(120)	唯一性	是	无	用户头像  
createTime	date		否	无	创建日期  
音乐表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	音乐ID  
name	varchar(20)		否	无	音乐标题  
musicUrl	varchar(100)		否	无	音乐资源  
albumId	int(11)		否	无	专辑ID  
artist	varchar(100)		否	无	歌手  
createTime	date		否	无	创建日期  
专辑表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	专辑ID  
name	varchar(50)		否	无	专辑名  
artistId	Int(11)		否	无	歌手ID  
albumDesc	Text		是	无	专辑介绍  
imgUrl	varchar(100)		否	无	专辑封面  
time	date		否	无	发表日期  
歌手表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	歌手ID  
name	varchar(20)		否	无	歌手名  
ImgUrl	varchar(10)		否	无	歌手封面  
artistDesc	varchar(100)		否	无	歌手介绍  
歌词表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	ID（自增）  
musicId	Int(11)		否	无	音乐ID  
lyrics	text		是	无	歌词  

歌手与歌曲关系表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	Id  
musicId	int(11)	外键	否	无	音乐ID  
artistId	int(11)	外键	否	无	歌手ID  

歌单表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	歌单ID  
userId	int(11)		否	无	用户ID  
title	varchar(100)		否	无	歌单标题  
artUri	varchar(225)		是	无	歌单封面  
sheetDesc	varchar(225)		是	无	歌单介绍  

歌单与音乐关系表  
字段名	数据类型	约束条件	是否可空	默认值	注释  
id	int(11)	主键	否	1	Id  
musicId	int(11)	外键	否	无	音乐Id  
sheetId	int(11)	外键	否	无	歌单Id  


