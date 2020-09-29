package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters

import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Highlight
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response

interface OnHighlightClickListener {
    fun onGameClicked(highlight: Highlight,position:Int)
}