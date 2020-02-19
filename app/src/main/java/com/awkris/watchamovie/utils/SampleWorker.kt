package com.awkris.watchamovie.utils

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class SampleWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val param =  inputData.getString(ARG_EXTRA_PARAM)
        Thread.sleep(1000)
        Timber.d( "Worker Test: doWork() called & param = $param")

        val outputData = createOutputData(param!!.length % 5, Math.pow(param.length.toDouble(), 4.0))
        return Result.success(outputData)
    }

    private fun createOutputData(firstData: Int, secondData: Double): Data {
        return Data.Builder()
            .putInt(OUTPUT_DATA_PARAM1, firstData)
            .putDouble(OUTPUT_DATA_PARAM2, secondData)
            .build()
    }

    companion object {
        const val ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM"
        const val OUTPUT_DATA_PARAM1 = "OUTPUT_DATA_PARAM1"
        const val OUTPUT_DATA_PARAM2 = "OUTPUT_DATA_PARAM2"
    }
}