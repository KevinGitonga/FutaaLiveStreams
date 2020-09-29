package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters

import android.content.Context
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Highlight
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.ResponseX
import ke.co.ipandasoft.futaasoccerlivestreams.utils.AppUtils
import timber.log.Timber

class UpcomingGamesAdapter(private val context: Context, private val onHighlightClickListener: OnHighlightClickListener, private val response: List<Highlight>): RecyclerView.Adapter<UpcomingGamesAdapter.GamesTodayVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesTodayVh {
        val gameView=LayoutInflater.from(parent.context).inflate(R.layout.upcoming_list_item,parent,false)
        return UpcomingGamesAdapter.GamesTodayVh(gameView)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: GamesTodayVh, position: Int) {
       val responseLocal=response[position]
        holder.gameDateTv.text=AppUtils.convertToLocalTime("${responseLocal.date} ${responseLocal.time}")
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
            holder.primaryLinkButton.visibility=View.GONE
            holder.primaryLinkButton.isClickable=false
            holder.awayTeamLogo.visibility=View.GONE
            holder.homeTeamLogo.visibility=View.GONE
            holder.leagueLogo.visibility=View.GONE
        }/*else if (locationData?.country!! == "Kenya" && locationData.countryCode.equals("KE")){
            holder.primaryLinkButton.visibility=View.GONE
            holder.primaryLinkButton.isClickable=false
            holder.awayTeamLogo.visibility=View.GONE
            holder.homeTeamLogo.visibility=View.GONE
        }*/

        Glide.with(context).load(imgBaseUrl+responseLocal.league.icon.trim()).centerCrop().into(holder.leagueLogo)
        Glide.with(context).load(imgBaseUrl+responseLocal.team_one.logo.trim()).centerCrop().into(holder.homeTeamLogo)
        Glide.with(context).load(imgBaseUrl+responseLocal.team_two.logo.trim()).centerCrop().into(holder.awayTeamLogo)

        holder.primaryLinkButton.setOnClickListener { onHighlightClickListener.onGameClicked(responseLocal,0) }

    }

    class GamesTodayVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val gameDateTv:TextView=itemView.findViewById(R.id.date)
     val homeTeamNameTv:TextView=itemView.findViewById(R.id.team_one_name)
     val awayTeamNameTv:TextView=itemView.findViewById(R.id.team_two_name)
     val leagueNameTv:TextView=itemView.findViewById(R.id.league_name)
     val leagueLogo:ImageView=itemView.findViewById(R.id.league_icon)
     val homeTeamLogo:ImageView=itemView.findViewById(R.id.team_one_ic)
     val awayTeamLogo:ImageView=itemView.findViewById(R.id.team_two_ic)
     val primaryLinkButton:MaterialButton=itemView.findViewById(R.id.watch_highlights)
    }
}