package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TweetsFragment.OnListFragmentInteractionListener] interface.
 */
class TweetsFragment : Fragment() {

    val api by lazy {
        Api.create()
    }
    var disposable: Disposable? = null
    var tweetsAdapter: TweetsAdapter? = null

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tweet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                tweetsAdapter = TweetsAdapter(listener)
                adapter = tweetsAdapter
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()

        disposable = api.listRepos()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> showTweets(result.tweets) },
                                    { error -> Toast.makeText(activity,"ERROR",Toast.LENGTH_LONG).show() }
                            )
    }

    private fun showTweets(tweets: Array<Tweet>) {
        tweetsAdapter!!.listTweets = tweets
        tweetsAdapter!!.notifyDataSetChanged()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                TweetsFragment()
    }
}
