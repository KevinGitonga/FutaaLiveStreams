package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters

import android.content.Context
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response
import ke.co.ipandasoft.futaasoccerlivestreams.utils.AppUtils
import ke.co.ipandasoft.futaasoccerlivestreams.utils.ExtraLinksParser
import timber.log.Timber

class TodayGamesAdapter(private val context: Context,private val onGameClickListener: OnGameClickListener,
                        private val response: List<Response>): RecyclerView.Adapter<TodayGamesAdapter.GamesTodayVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesTodayVh {
        val gameView=LayoutInflater.from(parent.context).inflate(R.layout.games_list_item,parent,false)
        return GamesTodayVh(gameView)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: GamesTodayVh, position: Int) {
       val responseLocal=response[position]
        holder.gameDateTv.text=AppUtils.convertToLocalTime("${responseLocal.date} ${responseLocal.time}")
        holder.timeAndVenueTv.text=
            "${AppUtils.convertToLocalTimeVenue("${responseLocal.date} ${responseLocal.time}")} - ${responseLocal.venue}"
        holder.homeTeamNameTv.text=responseLocal.team_one.name
        holder.awayTeamNameTv.text=responseLocal.team_two.name
        holder.leagueNameTv.text=responseLocal.league.name
        val imgBaseUrl:String=Hawk.get(Constants.SERVER_BASE_URL,Constants.SERVER_BASE_URL)+ Constants.IMG_ENDPOINT_URL
        val leagueLogoImg=imgBaseUrl+responseLocal.league.icon
        Timber.e("LEAGUE LOGO ${leagueLogoImg.trim()}")

        var locationData:LocationInfo?=null
        if (Hawk.contains(Constants.LOCATION_DATA_RESP_LOCAL)){
            locationData=Hawk.get(Constants.LOCATION_DATA_RESP_LOCAL)
        }
        Timber.e("DEVICE TIMEZONE ${locationData?.timezone}")

       if (locationData?.country!! == "United States" && locationData.countryCode.equals("US")){
            holder.isGameLiveIv.visibility=View.GONE
            holder.primaryLinkButton.visibility=View.GONE
            holder.linkOneButton.visibility=View.GONE
            holder.linkTwonButton.visibility=View.GONE
            holder.primaryLinkButton.isClickable=false
            holder.linkOneButton.isClickable=false
            holder.linkTwonButton.isClickable=false
            holder.linearGamesLayout.isClickable=false
            holder.awayTeamLogo.visibility=View.GONE
            holder.homeTeamLogo.visibility=View.GONE
            holder.leagueLogo.visibility=View.GONE
            Toast.makeText(context,"Upcoming Game Tips",Toast.LENGTH_SHORT).show()
        }
        /*else if (locationData?.country!! == "Kenya" && locationData.countryCode.equals("KE")){
           holder.isGameLiveIv.visibility=View.GONE
           holder.primaryLinkButton.visibility=View.GONE
           holder.linkOneButton.visibility=View.GONE
           holder.linkTwonButton.visibility=View.GONE
           holder.primaryLinkButton.isClickable=false
           holder.linkOneButton.isClickable=false
           holder.linkTwonButton.isClickable=false
           holder.linearGamesLayout.isClickable=false
           holder.awayTeamLogo.visibility=View.GONE
           holder.homeTeamLogo.visibility=View.GONE
           holder.leagueLogo.visibility=View.GONE
           Toast.makeText(context,"Upcoming Game Tips",Toast.LENGTH_SHORT).show()
       }*/

        if (responseLocal.is_live==0){
            holder.isGameLiveIv.visibility=View.GONE
            holder.primaryLinkButton.visibility=View.GONE
            holder.linkOneButton.visibility=View.GONE
            holder.linkTwonButton.visibility=View.GONE
        }
        Glide.with(context).load(imgBaseUrl+responseLocal.league.icon.trim()).centerCrop().into(holder.leagueLogo)
        Glide.with(context).load(imgBaseUrl+responseLocal.team_one.logo.trim()).centerCrop().into(holder.homeTeamLogo)
        Glide.with(context).load(imgBaseUrl+responseLocal.team_two.logo.trim()).centerCrop().into(holder.awayTeamLogo)

        if (!responseLocal.additional_links.isNullOrEmpty()){
            var extraLinks:ArrayList<String> =ExtraLinksParser.checkExtraStreams(responseLocal.additional_links)
            Timber.e("EXTRA LINKS" + extraLinks.size)
            if (extraLinks.size==1){
                responseLocal.channel.containsExtraLinks=true
                responseLocal.channel.linkOneExtra=extraLinks[0]
                holder.linkOneButton.setOnClickListener {
                    onGameClickListener.onGameClicked(responseLocal,2) }
            }
            else if(extraLinks.size==2){
                responseLocal.channel.containsExtraLinks=true
                responseLocal.channel.linkOneExtra=extraLinks[0]
                responseLocal.channel.linkTwoExtra=extraLinks[1]
                holder.linkOneButton.setOnClickListener {
                    onGameClickListener.onGameClicked(responseLocal,2) }
                holder.linkTwonButton.setOnClickListener {
                    onGameClickListener.onGameClicked(responseLocal,3) }

            }
        }
        holder.primaryLinkButton.setOnClickListener { onGameClickListener.onGameClicked(responseLocal,0) }
        holder.linkOneButton.setOnClickListener { onGameClickListener.onGameClicked(responseLocal,1) }
        holder.linkTwonButton.setOnClickListener { onGameClickListener.onGameClicked(responseLocal,2) }
        holder.linearGamesLayout.setOnClickListener{onGameClickListener.onGameClicked(responseLocal,3) }
    }

    class GamesTodayVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val linearGamesLayout:LinearLayout=itemView.findViewById(R.id.linearGamesLayout)
     val gameDateTv:TextView=itemView.findViewById(R.id.gameDate)
     val timeAndVenueTv:TextView=itemView.findViewById(R.id.time_and_venue)
     val isGameLiveIv:ImageView=itemView.findViewById(R.id.live_tag)
     val homeTeamNameTv:TextView=itemView.findViewById(R.id.team_one_name)
     val awayTeamNameTv:TextView=itemView.findViewById(R.id.team_two_name)
     val leagueNameTv:TextView=itemView.findViewById(R.id.league_name)
     val leagueLogo:ImageView=itemView.findViewById(R.id.league_icon)
     val homeTeamLogo:ImageView=itemView.findViewById(R.id.team_one_ic)
     val awayTeamLogo:ImageView=itemView.findViewById(R.id.team_two_ic)
     val primaryLinkButton:MaterialButton=itemView.findViewById(R.id.live_stream_sch_1)
     val linkOneButton:MaterialButton=itemView.findViewById(R.id.live_stream_sch_2)
     val linkTwonButton:MaterialButton=itemView.findViewById(R.id.live_stream_sch_3)
    }
}