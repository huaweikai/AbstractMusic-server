package com.hua.musicserver.bean

import com.hua.musicserver.noarg.NoArg

@NoArg
data class SheetBean(
    val id: Int,
    val userId: Int,
    val title: String,
    val artUri: String? = null,
    val sheetDesc: String? = null
)