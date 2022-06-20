package com.war_against_covid_19.safecampus.dbhelper

import android.provider.BaseColumns

object TrackingDB {

        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "attendance"
            const val COLUMN_NAME_ACCESS_ID = "access_id"
            const val COLUMN_NAME_QR_CONDITION = "qr_condition"

    }
}