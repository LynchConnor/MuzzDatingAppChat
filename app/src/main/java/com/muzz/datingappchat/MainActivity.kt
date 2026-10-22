package com.muzz.datingappchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.muzz.datingappchat.main.view.ChatView
import com.muzz.datingappchat.ui.theme.MuzzDatingAppChatTheme

/**
 * I have a ChatApp with 2 way communication. Where 2 people can communicate, these messages are sent
 * to each other. Messages from me should be pink and on the right. Messages from the other user should be on left.
 * Messages which are sent by the same person and 20s apart should have a smaller separation.
 *
 * For this app I will not include the back button as it's not necessary.
 * I will include the menu button to act as a way of switching between users,
 * and add a way to tap on the user as a quick switch.
 *
 * When the user starts writing we will highlight the text field and messages will go from bottom to top.
 *
 * I will use the colours from Muzz to copy the design in the screenshot.
 *
 * I will use Claude to build the mock so we can easily test the mock data with the functions we create.
 *
 * I will ignore the seen/read ticks on the messages. This wasn't included in the spec so I will ignore.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuzzDatingAppChatTheme {
                ChatView()
            }
        }
    }
}

@Preview
@Composable
private fun ChatPreview() {
    ChatView()
}