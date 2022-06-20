package com.war_against_covid_19.safecampus.storagehelper

import android.app.Application;

public class Storage : Application() {
    companion object {
        var email=""
        var buildingname=""
        var coursename=""
        var facemask=true
            }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }
}