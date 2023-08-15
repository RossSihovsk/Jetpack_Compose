package com.ross.jettrivia.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ross.jettrivia.component.Questions

@Composable
fun TriviaHome( viewModel: QuestionsViewModel = hiltViewModel()) = Questions(viewModel)
