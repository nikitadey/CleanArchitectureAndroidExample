/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nikita.demoapplication.app.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikita.demoapplication.ui.theme.DemoApplicationTheme


@Composable
fun DemoApp() {
    DemoApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            RowList()
        }
    }
}

@Composable
fun RowList() {

    val scrollState = rememberScrollState()

    Row(Modifier.horizontalScroll(scrollState)) {
        repeat(50) {
            Button(
                onClick = { },
                border = BorderStroke(1.dp, Color.DarkGray),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(10.dp),
                modifier =  Modifier
                    .padding(8.dp)

            )
            {
                Text(
                    " $it ",
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(5.dp),
                    color = Color.LightGray
                )
            }

        }
    }


}