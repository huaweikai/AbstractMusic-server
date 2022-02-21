package com.hua.musicserver.controller

import cn.dev33.satoken.util.SaResult
import com.hua.musicserver.dao.SheetManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

@RestController
@RequestMapping("/sheet")
class SheetController {

    private val bannerMap = HashMap<String,List<Int>>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Autowired
    lateinit var sheetManagerMapper: SheetManagerMapper

    @GetMapping("/recommend")
    fun recommendSheet():SaResult{
        return SaResult.data(sheetManagerMapper.recommendList())
    }

    @GetMapping("/recommend/{id}/list")
    fun getRecommendList(
        @PathVariable("id") id:String
    ):SaResult{
        return SaResult.data(sheetManagerMapper.getRecommendList(id))
    }

    @GetMapping("/banner")
    fun getBanner():SaResult{
        val date = dateFormat.format(Date(System.currentTimeMillis()))
        val listId = if (bannerMap.containsKey(date)){
            bannerMap[date]!!
        }else{
            val list = arrayListOf<Int>()
            while (list.size < 3){
                val id = Random.nextInt(1,9)
                if(id !in list){
                    list.add(id)
                }
            }
            bannerMap[date] = list
            list
        }
        return SaResult.data(sheetManagerMapper.selectBanner(listId[0],listId[1],listId[2]))
    }
}