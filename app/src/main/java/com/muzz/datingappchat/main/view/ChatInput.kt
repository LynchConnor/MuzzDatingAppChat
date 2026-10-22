package com.muzz.datingappchat.main.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.muzz.datingappchat.R

@Composable
fun ChatInput(
    placeholder: String,
    onSend: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var draft by rememberSaveable { mutableStateOf("") }
    val canSend = draft.isNotBlank()
    val colors = MaterialTheme.colorScheme
    val borderColor by animateColorAsState(if (canSend) colors.primary else colors.outlineVariant)

    fun send() {
        if (!canSend) return
        onSend(draft.trim())
        draft = ""
    }

    Column(modifier) {
        HorizontalDivider(color = colors.outlineVariant)
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, borderColor, CircleShape)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurface),
                cursorBrush = SolidColor(colors.primary),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(onSend = { send() }),
                maxLines = 4,
                decorationBox = { field ->
                    Box {
                        if (draft.isEmpty()) {
                            Text(placeholder, color = colors.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                        }
                        field()
                    }
                },
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = ::send,
                enabled = canSend,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.primary.copy(alpha = 0.4f),
                    disabledContentColor = colors.onPrimary,
                ),
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.send))
            }
        }
    }
}
