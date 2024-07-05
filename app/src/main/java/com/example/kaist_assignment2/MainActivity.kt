package com.example.kaist_assignment2

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kaist_assignment2.ui.theme.Kaist_Assignment2Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kaist_Assignment2Theme {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Hello World")

                    Button(
                        onClick = {
                            val intent = Intent(this@MainActivity, AuthCodeHandlerActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Login with Kakao")
                    }
                }
            }
        }
    }
}