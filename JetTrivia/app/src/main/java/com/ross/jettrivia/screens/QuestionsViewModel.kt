package com.ross.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ross.jettrivia.data.DataWrapper
import com.ross.jettrivia.model.QuestionItem
import com.ross.jettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repository: QuestionRepository) : ViewModel() {
    val data: MutableState<DataWrapper<ArrayList<QuestionItem>>> = mutableStateOf(
        DataWrapper(questions = null, loading = true, e = null)
    )


    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            data.value.questions?.let { list ->
                data.value.loading = list.isEmpty()
            }
        }

    }

    fun getTotalQuestionCount(): Int {
        return data.value.questions?.toMutableList()?.size!!
    }


}