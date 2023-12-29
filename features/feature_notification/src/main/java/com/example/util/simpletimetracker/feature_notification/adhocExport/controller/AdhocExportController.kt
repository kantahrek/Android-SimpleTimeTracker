package com.example.util.simpletimetracker.feature_notification.adhocExport.controller

import com.example.util.simpletimetracker.domain.interactor.PrefsInteractor
import com.example.util.simpletimetracker.domain.interactor.RecordTypeInteractor
import com.example.util.simpletimetracker.domain.interactor.RunningRecordInteractor
import com.example.util.simpletimetracker.domain.mapper.AppColorMapper
import com.example.util.simpletimetracker.feature_notification.adhocExport.model.AdhocExportRecordModel
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AdhocExportController @Inject constructor(
    private val prefsInteractor: PrefsInteractor,
    private val recordTypeInteractor: RecordTypeInteractor,
    private val runningRecordInteractor: RunningRecordInteractor,
    private val appColorMapper: AppColorMapper,
) {
    fun exportRecordsToJson(): String {
        return runBlocking {
            val gson = Gson()
            val activityList = recordTypeInteractor.getAll()
            val runningRecords = runningRecordInteractor.getAll()

            val exportRecords = activityList
                .filter { recordType -> !recordType.hidden }
                .map { recordType ->
                    AdhocExportRecordModel(
                        recordType.id,
                        recordType.name,
                        recordType.icon,
                        appColorMapper.mapToColorInt(recordType.color),
                        recordType.hidden,
                    )
                }
                .associateBy { model -> model.id }

            runningRecords.forEach { runningRecord ->
                val matchingRecord = exportRecords[runningRecord.id]
                matchingRecord?.timeStarted = runningRecord.timeStarted
            }

            gson.toJson(exportRecords)
        }
    }

    fun exportPrefs(): Boolean {
        return runBlocking {
            prefsInteractor.getAllowMultitasking()
        }
    }
}