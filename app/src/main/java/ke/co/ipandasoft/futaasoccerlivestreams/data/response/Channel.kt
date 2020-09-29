package ke.co.ipandasoft.futaasoccerlivestreams.data.response

data class Channel(
    val allow_free: Int,
    val created_at: String,
    val id: Int,
    val is_live: Int,
    val logo: String,
    val name: String,
    var stream: String,
    val updated_at: String,
    var containsExtraLinks:Boolean=false,
    var linkOneExtra:String="null",
    var linkTwoExtra:String="null",
    var clickPosition:Int
)