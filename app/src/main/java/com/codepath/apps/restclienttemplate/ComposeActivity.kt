package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.TimelineActivity.Companion.TAG
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var buttonTweet: Button
    lateinit var characterCounter: TextView

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1DA1F2")))

        etCompose = findViewById(R.id.tweet)
        buttonTweet = findViewById(R.id.buttonTweet)
        characterCounter = findViewById(R.id.characterCount)

        client = TwitterApplication.getRestClient(this)

        buttonTweet.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1DA1F2")))

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                characterCounter.text = s.length.toString()


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
            }


        })

        buttonTweet.setOnClickListener{


            val tweetContent = etCompose.text.toString()

            if(tweetContent.length == 0) {
                Toast.makeText(this, "Tweet cannot be of length 0", Toast.LENGTH_SHORT).show()

            }

            else if(tweetContent.length > 280) {
                Toast.makeText(this, "Tweet is too long. The limit is 140 characters", Toast.LENGTH_SHORT).show()
            }

            else {
                client.postTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
            }


        }


    }

    companion object {
        val TAG = "ComposeActivity"
    }
}