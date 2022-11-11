package org.gsm.autocrypttest.data.model

import org.gsm.autocrypttest.data.model.Data

data class ResponseData(
    var currentCount: Int,
    var data: List<Data>,
    var matchCount: Int,
    var page: Int,
    var perPage: Int,
    var totalCount: Int
)