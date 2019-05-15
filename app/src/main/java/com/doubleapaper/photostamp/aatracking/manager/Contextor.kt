package com.doubleapaper.photostamp.aatracking.manager

import android.content.Context

/**
 * Created by nuuneoi on 12/6/14 AD.
 */
class Contextor private constructor() {

    var context: Context? = null
        private set

    fun init(context: Context) {
        this.context = context
    }

    companion object {

        private var instance: Contextor? = null

        fun getInstance(): Contextor {
            if (instance == null)
                instance = Contextor()
            return instance!!
        }
    }

}


/*private static Contextor instance;

    public static Contextor getInstance() {
        if (instance == null)
            instance = new Contextor();
        return instance;
    }

    private Context mContext;

    private Contextor() {

    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }*/



