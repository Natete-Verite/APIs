package dev.verite.myposts

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.verite.myposts.databinding.ActivityCommentsBinding
import dev.verite.myposts.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsActivity : AppCompatActivity() {
    var postId = 0
    lateinit var binding: ActivityCommentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolabar()
        obtainPostId()
        fetchPostById()
        fetchComments()
    }
    fun obtainPostId(){
        postId = intent.extras?.getInt("POST_ID")?:0
    }
    fun fetchPostById(){
        val apiClient = ApiClient.buildApiClient(ApiInterface::class.java)
        val request = apiClient.getPostsById(postId)
        request.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                var post = response.body()
                binding.tvPostTitle.text = post?.title
                binding.tvPostBody.text = post?.body
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(baseContext,t.message,Toast.LENGTH_LONG)
            }
        })
    }
    fun setupToolabar(){
        setSupportActionBar(binding.toolbarComments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun fetchComments(){
        val apiClient = ApiClient.buildApiClient(ApiInterface::class.java)
        val request = apiClient.getComments()
        request.enqueue(object: Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful){
                    var comments=response.body()
                    Toast.makeText(baseContext,"fetched ${comments!!.size}posts",Toast.LENGTH_LONG).show()
                    var adapter=CommentsRvAdapter(comments)
                    Log.d("Tag",comments.toString())
                    binding.rvComments.adapter=adapter
                    binding.rvComments.layoutManager= LinearLayoutManager(baseContext)
                }
            }
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {

            }
        })
    }
}