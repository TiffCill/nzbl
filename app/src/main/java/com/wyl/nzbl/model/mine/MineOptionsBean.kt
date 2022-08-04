package com.wyl.nzbl.model.mine

data class MineOptionsData(
    val name: String,
    val datas: List<DetailsOptionsData>
)

data class DetailsOptionsData(
    val optionName: String,
    val imageUrl: String
)