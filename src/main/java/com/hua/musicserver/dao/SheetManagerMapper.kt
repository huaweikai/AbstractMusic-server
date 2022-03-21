package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import com.hua.musicserver.bean.SheetBean
import org.apache.ibatis.annotations.*

const val sheetBean = " sheet.*,(select count(*) from sheettomusic where sheet.id = sheettomusic.sheetId) as 'num',(select `user`.`name` from `user` where sheet.userId=id) as 'author' "

@Mapper
interface SheetManagerMapper {

    @Select(
        "select $albumBean FROM albumlist,artistlist limit #{start},#{end}"
    )
    fun selectBanner(start: Int,end: Int): List<AlbumBean>

    @Select("select count(*) from albumlist")
    fun selectAlbumCount():Int

    @Select(
        "select $sheetBean from sheet LIMIT #{start},#{end}"
    )
    fun recommendList(start:Int,end:Int):List<SheetBean>

    @Select(
        "SELECT $musicBean FROM musicList,albumlist,sheettomusic where musiclist.albumId = albumlist.id " +
                "and sheettomusic.sheetId = #{id} and sheettomusic.musicId = musiclist.id order by musiclist.createTime desc"
    )
    fun getMusicBySheetId(id:String):List<MusicBean>


    @Select("select $sheetBean from sheet where userId = #{userId}")
    fun getUserSheet(userId: String):List<SheetBean>


    @Insert("insert into sheettomusic(sheetId,musicId) value(#{sheetId},#{musicId})")
    fun insertUserSheet(sheetId:String,musicId:String):Int

    @Insert("INSERT into sheet(sheet.title,sheet.userId) value(#{title},#{userId})")
    fun createNewSheet(title:String,userId:String):Int

    @Select("select musicId from sheettomusic where sheetId = #{sheetId}")
    fun selectMusicIdBySheetId(sheetId: String):List<String>

    @Select("select title from sheet where userId = #{userId}")
    fun selectTitleByUser(userId:String):List<String>

    @Select("select userId from sheet where id = #{sheetId}")
    fun selectUserIdBySheetId(sheetId: String):String

    @Delete("delete from sheettomusic where sheetId =#{sheetId} and musicId = #{musicId}")
    fun deleteMusicFromSheet(sheetId: String,musicId: String)

    @Delete("delete from sheettomusic where sheetId = #{sheetId}")
    fun deleteAllMusicFromSheet(sheetId: String)

    @Delete("delete from sheet where id = #{sheetId}")
    fun deleteSheet(sheetId: String)

    @Update("update sheet set title = #{title},artUri = #{artUri},sheetDesc= #{sheetDesc} where id = #{id}")
    fun updateSheet(sheetBean: SheetBean)

    @Select("select $sheetBean from sheet where id = #{sheetId}")
    fun selectSheetBySheetId(sheetId: String):SheetBean?

    @Select("select $sheetBean from sheet where title like #{name}")
    fun selectSheetByName(name:String):List<SheetBean>

    @Select("select count(*) from sheet")
    fun selectSheetNum():Int

}