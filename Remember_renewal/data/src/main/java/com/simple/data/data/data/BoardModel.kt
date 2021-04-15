package com.simple.data.data.data

data class DataWrite (var title : String = "", var contents:String = "",
                      var date : String = "", var imgContents : String? = null, var liked : Int = 0, var reported : Int = 0,
                      var viewing : Boolean = true, var users : String ="", var displayName : String = "")

data class ReportedData(var reporter : String = "", var reported_contents :  String ="")