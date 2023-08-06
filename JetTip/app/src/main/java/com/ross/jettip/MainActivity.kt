@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.ross.jettip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ross.jettip.components.InputField
import com.ross.jettip.ui.theme.JetTipTheme
import com.ross.jettip.util.calculateTotalPerPerson
import com.ross.jettip.util.calculateTotalTip
import com.ross.jettip.util.formatDoubleToString
import com.ross.jettip.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {
                MyApp {
                    MainContent()
                }
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        content()
    }
}

//@Preview
@Composable
fun MainContent() {

    val splitByState = remember {
        mutableStateOf(1)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm(
            splitByState = splitByState, tipAmountState = tipAmountState, totalPerPerson = totalPerPerson
        )
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    onValChanged: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyBoardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    Surface(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {


        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TopHeader(tipsPerPerson = totalPerPerson.value)
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingle = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChanged(totalBillState.value.trim())
                    keyBoardController?.hide()
                })
            if (validState) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split", modifier = Modifier.align(Alignment.CenterVertically))

                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp), horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            splitByState.value = if (splitByState.value > 1) splitByState.value - 1 else 1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercentage = tipPercentage
                            )
                        })

                        Text(
                            text = "${splitByState.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            if (splitByState.value < range.last) splitByState.value = splitByState.value + 1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercentage = tipPercentage
                            )
                        })
                    }
                }
                CreateTip(tipAmountState)

                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$tipPercentage %")
                    Spacer(modifier = Modifier.height(14.dp))

                    CreateSlider(
                        sliderPositionState,
                        totalPerPerson,
                        splitByState,
                        tipPercentage,
                        totalBillState,
                        tipAmountState
                    )

                }

            } else {
                Box {}
            }
        }
    }
}

@Composable
fun CreateTip(tipAmountState: MutableState<Double>) {
    //Tip
    Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
        Text(
            text = "Tip", modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(200.dp))

        Text(text = "$ ${tipAmountState.value}", modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun CreateSlider(
    sliderPositionState: MutableState<Float>,
    totalPerPerson: MutableState<Double>,
    splitByState: MutableState<Int>,
    tipPercentage: Int,
    totalBillState: MutableState<String>,
    tipAmountState: MutableState<Double>
) {
    Slider(
        value = sliderPositionState.value, onValueChange = { newVal ->
            sliderPositionState.value = newVal
            tipAmountState.value = calculateTotalTip(
                totalBill = totalBillState.value.toDouble(), tipPercentage = tipPercentage
            )
            totalPerPerson.value = calculateTotalPerPerson(
                totalBill = totalBillState.value.toDouble(),
                splitBy = splitByState.value,
                tipPercentage = tipPercentage
            )
        }, modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        steps = 9
    )
}

@Composable
fun TopHeader(tipsPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))), color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = formatDoubleToString(tipsPerPerson)
            Text(
                text = "Total Per Person", style = MaterialTheme.typography.h5
            )
            Text(
                text = "$$total", style = MaterialTheme.typography.h4, fontWeight = FontWeight.Bold
            )

        }

    }
}

