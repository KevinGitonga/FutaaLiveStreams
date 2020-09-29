package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters

import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response

interface OnGameClickListener {
    fun onGameClicked(response: Response,position:Int)
}