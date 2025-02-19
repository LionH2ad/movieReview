package com.example.movieReview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieReview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val reviewList = mutableListOf<Review>()
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 초기화
        adapter = ReviewAdapter(reviewList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        // 리뷰 추가 버튼 클릭 이벤트
        binding.btnAddReview.setOnClickListener {
            addReview()
        }

        // 정렬 옵션 선택 이벤트
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortReviews()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 선택하지 않은 경우
            }
        }
    }

    private fun addReview() {
        val review = run {
            val title = binding.etTitle.text.toString().let {
                it.replaceFirstChar { char -> char.uppercase() }
            }
            val author = binding.etAuthor.text.toString().let {
                it.uppercase()
            }
            val content = binding.etContent.text.toString().let {
                it.split(". ").joinToString(". ") { sentence ->
                    sentence.replaceFirstChar { char -> char.uppercase() }
                }
            }
            Review(title, author, content)
        }

        reviewList.run {
            if (none { it.title == review.title && it.author == review.author }) {
                add(review)
            } else {
                Log.e("DuplicateError", "중복된 리뷰입니다.")
            }
        }.also {
            adapter.updateList(reviewList)
        }
    }

    private fun sortReviews() {
        val sortedList = with(reviewList) {
            when (binding.spinnerSort.selectedItem.toString()) {
                "제목순" -> sortedBy { it.title }
                "작성자순" -> sortedBy { it.author }
                else -> this
            }
        }
        adapter.updateList(sortedList)
    }
}
