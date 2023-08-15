package com.ross.jettrivia.repository

import com.ross.jettrivia.data.DataWrapper
import com.ross.jettrivia.model.QuestionItem
import com.ross.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException =
        DataWrapper<ArrayList<QuestionItem>>()

    suspend fun getAllQuestions(): DataWrapper<ArrayList<QuestionItem>> {
        try {
            dataOrException.loading = true
            dataOrException.questions = api.getAllQuestions()
            dataOrException.questions?.let { questionItems ->
                if (questionItems.isNotEmpty()) dataOrException.loading = false
            }

        } catch (exception: Exception) {
            dataOrException.e = exception
        }
        return dataOrException
    }
}