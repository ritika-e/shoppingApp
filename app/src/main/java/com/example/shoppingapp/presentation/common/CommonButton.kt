package com.example.shoppingapp.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonButton(
    text:String,
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.medium,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    fontSize:Float = 16f,
    testTag: String? = null
){
    Button(
        onClick = onClick,
        modifier = modifier.then(
            if (testTag != null) Modifier.testTag(testTag) else Modifier // Apply testTag if provided
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = shape,
        contentPadding = padding
    )  {
        
        Text(text = text,
            style = TextStyle(fontSize = fontSize.sp)
        )
        
    }
}