package com.botigocontigo.alfred

import android.support.v4.app.Fragment
import com.botigocontigo.alfred.areas.AreasFragment
import com.botigocontigo.alfred.foda.FodaFragment
import com.botigocontigo.alfred.learn.LearnFragment

class AppFragments {

    object FRAGMENTS {
        operator fun get(position: Int): Fragment {
            return when(position) {
                //TODO replace each with its Fragment class
                0 -> InterviewFragment()
//                1 -> TODO_FRAGMENT_SUGGESTIONS
                2 -> TasksFragment()
                3 -> LearnFragment()
                4 -> AreasFragment()
                5 -> FodaFragment()
//                6 -> TODO_FRAGMENT_RISKS
                else -> InterviewFragment()
            }
        }
    }

    object POSITION {
        operator fun get(relativeUrl: String): Int {
            return when(relativeUrl) {
                "/chat" -> 0
                "/tasks" -> 2
                "/learn" -> 3
                "/swot" -> 5
                else -> 0 // go to interview, by default
            }
        }
    }

}
