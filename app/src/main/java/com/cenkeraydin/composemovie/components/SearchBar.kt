package com.cenkeraydin.composemovie.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cenkeraydin.composemovie.R

@Composable
fun ModernSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String ,
    isActive: Boolean = false,
    onActiveChange: (Boolean) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val isDark = isSystemInDarkTheme()

    val backgroundColor = if (isDark) {
        Color(0xFF1C1B23)
    } else {
        Color(0xFFF8FAFC)
    }

    val borderColor = if (isDark) {
        if (isActive) Color(0xFF7C83FD) else Color(0xFF2E2E3E) // aktifken morumsu canlı
    } else {
        if (isActive) Color(0xFF6366F1) else Color(0xFFE5E7EB)
    }

    val textColor = if (isDark) {
        Color(0xFFF4F4F5)
    } else {
        Color(0xFF1F2937)
    }

    val placeholderColor = if (isDark) {
        Color(0xFF9FA3B6)
    } else {
        Color(0xFF6B7280)
    }

    val iconColor = if (isDark) {
        Color(0xFFB0B4C3)
    } else {
        Color(0xFF6B7280)
    }

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isActive) 10.dp else 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = if (isActive) 2.dp else 1.dp,
                color = borderColor
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon
                leadingIcon?.invoke() ?: run {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search",
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Search TextField
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            onActiveChange(focusState.isFocused)
                        },
                    enabled = enabled,
                    singleLine = true,
                    cursorBrush = SolidColor(if (isDark) Color.White else Color.Black),
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(query)
                            keyboardController?.hide()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = placeholderColor,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )

                // Trailing Icons
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Clear Button
                        IconButton(
                            onClick = {
                                onQueryChange("")
                                onActiveChange(false)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Clear",
                                tint = iconColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Search Button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF6366F1),
                                            Color(0xFF8B5CF6)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clickable {
                                    onSearch(query)
                                    keyboardController?.hide()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                trailingIcon?.invoke()
            }
        }
    }
}
