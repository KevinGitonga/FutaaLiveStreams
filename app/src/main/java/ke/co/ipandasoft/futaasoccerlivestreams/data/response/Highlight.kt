package ke.co.ipandasoft.futaasoccerlivestreams.data.response

data class Highlight(
    val additional_links: Any,
    val channel: Channel,
    val channel_id: Int,
    val country_restriction: String,
    val created_at: String,
    val date: String,
    val highlights_url: Any,
    val id: Int,
    val is_live: Int,
    val league: League,
    val league_id: Int,
    val other_headers: Any,
    val primary_headers: Any,
    val rescriction: Any,
    val team_one: TeamOne,
    val team_two: TeamTwo,
    val time: String,
    val updated_at: String,
    val venue: String
)