package com.mstc.mstcapp.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.withTransaction
import com.mstc.mstcapp.MainActivity
import com.mstc.mstcapp.model.explore.BoardMember
import com.mstc.mstcapp.model.resource.Roadmap
import com.mstc.mstcapp.util.Constants
import com.mstc.mstcapp.util.RetrofitService
import java.util.*

private const val TAG = "ResourceRepository"

class ResourceRepository(
    private val context: Context,
    private val stcDatabase: STCDatabase,
) {
    private var service: RetrofitService = RetrofitService.create()
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE)

    fun getBoardMembers(): LiveData<List<BoardMember>> = liveData {
        emitSource(stcDatabase.databaseDao().getBoardMembers())
        val lastChecked: Long = sharedPreferences.getLong("lastChecked", -1)
        var nextCheck = System.currentTimeMillis()
        if (lastChecked != -1L) {
            val cal = Calendar.getInstance()
            cal.time = Date(lastChecked)
            cal.add(Calendar.DAY_OF_MONTH, 180)
            nextCheck = cal.time.time
        }
        if (lastChecked == -1L || nextCheck <= Date().time)
            if (isNetworkAvailable(context))
                fetchBoardMembers()
    }

    private suspend fun fetchBoardMembers() {
        val response = service.getBoard()
        if (response.isSuccessful) {
            Log.d(TAG, "getBoardMembers() returned: ${response.body()}")
            stcDatabase.databaseDao().clearBoardMembers()
            response.body()?.let { stcDatabase.databaseDao().insertBoardMembers(it) }
        } else Log.e(TAG, "getBoardMembers: " + response.code())
    }


    fun getDomainDetails(domain: String) = liveData {
        if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_details")!!) {
            fetchDetails(domain)
        }
        emitSource(stcDatabase.databaseDao().getDetails(domain))
    }

    private suspend fun fetchDetails(domain: String) {
        val response = service.getDetails(domain)
        if (response.isSuccessful) {
            Log.d(TAG, "getBoardMembers() returned: ${response.body()}")
            stcDatabase.withTransaction {
                stcDatabase.databaseDao().deleteDetails(domain)
                response.body()?.let { stcDatabase.databaseDao().insertDetails(it) }
                MainActivity.setFetchedData(domain + "_details")
            }
        } else Log.e(TAG, "getBoardMembers: " + response.code())

    }

    fun getDomainRoadmap(domain: String) = liveData<Roadmap> {
        emitSource(stcDatabase.databaseDao().getRoadmap(domain))
        if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_roadmap")!!) {
            fetchRoadmap(domain)
        }
    }

    private suspend fun fetchRoadmap(domain: String) {
        val response = service.getRoadmap(domain)
        if (response.isSuccessful) {
            Log.d(TAG, "fetchRoadmap() returned: ${response.body()}")
            stcDatabase.withTransaction {
                stcDatabase.databaseDao().deleteRoadmap(domain)
                response.body()?.let { stcDatabase.databaseDao().insertRoadmap(it) }
                MainActivity.setFetchedData(domain + "_roadmap")
            }
        } else Log.e(TAG, "fetchRoadmap: " + response.code())
    }

    fun getDomainResources(domain: String) = liveData {
        emitSource(stcDatabase.databaseDao().getResources(domain))
        if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_resources")!!) {
            fetchResources(domain)
        }
    }

    private suspend fun fetchResources(domain: String) {
        val response = service.getResources(domain)
        if (response.isSuccessful) {
            Log.d(TAG, "getBoardMembers() returned: ${response.body()}")
            stcDatabase.withTransaction {
                response.body()?.let {
                    stcDatabase.databaseDao().deleteResources(domain)
                    stcDatabase.databaseDao().insertResources(it)
                    MainActivity.setFetchedData(domain + "_resources")
                }
            }
        } else Log.e(TAG, "getBoardMembers: " + response.code())
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    suspend fun refreshDetails(domain: String) {
        fetchDetails(domain)
    }

    suspend fun refreshRoadmap(domain: String) {
        fetchRoadmap(domain)
    }

    suspend fun refreshResources(domain: String) {
        fetchResources(domain)
    }

    suspend fun refreshBoard() {
        fetchBoardMembers()
    }
}