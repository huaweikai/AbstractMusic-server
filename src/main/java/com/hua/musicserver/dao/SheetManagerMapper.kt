package com.hua.musicserver.dao

import com.hua.musicserver.bean.AlbumBean
import com.hua.musicserver.bean.MusicBean
import com.hua.musicserver.bean.SheetBean
import org.apache.ibatis.annotations.*

@Mapper
interface SheetManagerMapper {

    @Select(
        "select albumlist.*,artistlist.`name` as artistName FROM albumlist,artistlist " +
                "WHERE (albumlist.id = #{id1} or albumlist.id =#{id2} or albumlist.id = #{id3}) " +
                "and albumlist.artistId = artistlist.id"
    )
    fun selectBanner(id1: Int, id2: Int, id3: Int): List<AlbumBean>

    @Select(
        "select * from sheet where userId = 0"
    )
    fun recommendList():List<SheetBean>

    @Select(
        "SELECT musiclist.id,musiclist.`name`,albumlist.imgUrl AS imgUrl,musiclist.musicUrl,musiclist.albumId,albumlist.`name` as albumName,artist " +
                "FROM musicList,albumlist,sheettomusic where musiclist.albumId = albumlist.id " +
                "and sheettomusic.sheetId = #{id} and sheettomusic.musicId = musiclist.id order by musiclist.createTime desc"
    )
    fun getMusicBySheetId(id:String):List<MusicBean>


    @Select("select * from sheet where userId = #{userId}")
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

    @Select("select * from sheet where id = #{sheetId}")
    fun selectSheetBySheetId(sheetId: String):SheetBean

    @Select("select * from sheet where title like #{name}")
    fun selectSheetByName(name:String):List<SheetBean>

}