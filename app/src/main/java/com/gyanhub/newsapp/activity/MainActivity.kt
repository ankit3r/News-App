package com.gyanhub.newsapp.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.newsapp.R
import com.gyanhub.newsapp.adapters.ArticleAdapter
import com.gyanhub.newsapp.adapters.CategoryAdapter
import com.gyanhub.newsapp.adapters.ReadMore
import com.gyanhub.newsapp.adapters.SelectCategory
import com.gyanhub.newsapp.databinding.ActivityMainBinding
import com.gyanhub.newsapp.newsApplication.NewsApplication
import com.gyanhub.newsapp.utils.NetworkResult
import com.gyanhub.newsapp.viewModels.MainViewFactory
import com.gyanhub.newsapp.viewModels.MainViewModel
import com.gyanhub.tmdb_api.utils.NetworkUtils.Companion.isInternetAvailable

@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity() ,SelectCategory,ReadMore{
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var searchView: SearchView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsAdapter: ArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var shortBy : String
    private lateinit var shortByCountry : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        title = null

        // THIS IS REFERENCE OF REPOSITORY CLASS
        val repository = (application as NewsApplication).newsRepository

        // THIS IS AN OBJECT OF VIEW MODEL CLASS
        mainViewModel = ViewModelProvider(this, MainViewFactory(repository))[MainViewModel::class.java]

        // THIS METHODE USE TO GET NEWS
        getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)

        // THIS IS USE FOR SCROLL CONTROL
        mainBinding.rcViewArt.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > -0) {
                    mainBinding.rcCat.visibility = View.GONE
                    mainBinding.txtTool.visibility = View.GONE
                }
                else {
                    mainBinding.rcCat.visibility = View.VISIBLE
                   if(mainViewModel.toolVisibility) mainBinding.txtTool.visibility = View.VISIBLE
                   else mainBinding.txtTool.visibility = View.GONE
                }
            }
        })

        // THIS CLICK ALLOW TO USER FILTER TOP HEADLINE
        mainBinding.cardTxtType.setOnClickListener {
            if(mainBinding.txtTool.visibility == View.VISIBLE){
                mainBinding.txtTool.visibility = View.GONE
                mainViewModel.toolVisibility = false
            }else{
                mainBinding.txtTool.visibility = View.VISIBLE
                mainViewModel.toolVisibility = true
            }
        }

        // GATING CATEGORY LIST DATA FROM VIEW MODEL CLASS
        mainViewModel.categoryLiveData.observe(this){
            categoryAdapter = CategoryAdapter(it,this,mainViewModel._selectedPosition)
            mainBinding.rcCat.adapter = categoryAdapter
        }

        // GATING FILTER BY LIST DATA FROM VIEW MODEL CLASS
        mainViewModel.shortByLiveData.observe(this){
          dropdown(mainBinding.spinnerDate,it,1)
        }

        // GATING FILTER COUNTRY LIST DATA FROM VIEW MODEL CLASS
        mainViewModel.shortByCountLiveData.observe(this){
            dropdown(mainBinding.spinnerDate2,it,2)
        }

        // THIS IS USE TO REFRESH NEWS
        mainBinding.swipeRefreshLayout.setOnRefreshListener {

            mainBinding.swipeRefreshLayout.isRefreshing = true
            getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
            mainBinding.swipeRefreshLayout.isRefreshing = false
        }

        // THIS IS USE TO FILTER NEWS
        mainBinding.txtFilter.setOnClickListener {

            getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
          mainBinding.txtTool.visibility = View.VISIBLE
            mainViewModel.toolVisibility = true
          mainBinding.filterLayout.visibility = View.GONE
        }

        // THIS IS USE TO SHOW FILTER LAYOUT
        mainBinding.txtTool.setOnClickListener {
            it.visibility = View.GONE
            mainViewModel.toolVisibility = false
            mainBinding.filterLayout.visibility = View.VISIBLE
        }

        // GATING NEWS FROM VIEW MODEL CLASS
        mainViewModel.data.observe(this) {
            when(it){
                is NetworkResult.Success -> {
                    mainBinding.rcViewArt.visibility=View.VISIBLE
                    progress()
                    newsAdapter = ArticleAdapter(it.data!!.articles,this,this,)
                    mainBinding.rcViewArt.adapter = newsAdapter
                    newsAdapter.notifyDataSetChanged()
                }
                is NetworkResult.Error -> {
                    progress()
                    massagerContent(it.massage!!)
                }
                is NetworkResult.Loading -> {
                    progress(View.VISIBLE)
                }
            }
        }

        // ERROR HANDLING RELOAD
        mainBinding.btnRetry.setOnClickListener {
            getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
        }
    }

    // THIS IS USE FOR SEARCH MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search News"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                categoryAdapter.selectedPosition = RecyclerView.NO_POSITION
                categoryAdapter.notifyDataSetChanged()
                mainViewModel.searchText = query!!
                hideKeyboard(searchView)
                mainViewModel.type = "search"
                getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
                newsAdapter.notifyDataSetChanged()
                if(mainBinding.filterLayout.visibility != View.VISIBLE){
                    mainBinding.txtTool.visibility = View.VISIBLE
                    mainViewModel.toolVisibility = true
                }else mainViewModel.toolVisibility = false
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    // THIS METHODE USE TO GET NEWS
    private fun getNews(type:String, cats:String, searchText:String){
        mainBinding.btnRetry.visibility = View.GONE
        mainBinding.txtErrorMassage.visibility = View.GONE
        mainBinding.rcViewArt.visibility=View.GONE
        shortBy = mainViewModel.shortBy
        shortByCountry = mainViewModel.shortByCountry

        if(isInternetAvailable(this)){
            when(type){
                "category" -> {
                    mainBinding.txtType.text = cats
                    val url = "${getString(R.string.baseUrl)}top-headlines?country=$shortByCountry&category=$cats&apiKey=${getString(R.string.apiKey)}"
                    mainViewModel.getNews(url)
                }
                "search" -> {
                    mainBinding.txtType.text = searchText
                    val url = "${getString(R.string.baseUrl)}everything?q=$searchText&sortBy=$shortBy?country=$shortByCountry&apiKey=${getString(R.string.apiKey)}"
                    mainViewModel.getNews(url)
                }
                "top" -> {
                    mainBinding.txtType.text = getString(R.string.headline)
                    mainViewModel.getNews("${getString(R.string.baseUrl)}top-headlines?country=$shortByCountry&apiKey=${getString(R.string.apiKey)}")
                }
            }
        }else{
            massagerContent(getString(R.string.networkError))
        }
    }

    // THIS METHODE USE TO HIDE KEYBOARD
    fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    // THIS METHODE CALL TO SHOW ERROR MASSAGE AND HIDE MASSAGE
    private fun massagerContent(massage:String){
        mainBinding.btnRetry.visibility = View.VISIBLE
        mainBinding.txtErrorMassage.visibility = View.VISIBLE
        mainBinding.txtErrorMassage.text = massage
    }

    //THIS METHODE USE FOR HIDE PROGRESS BAR AND SHOW PROGRESS BAR
    private fun progress(view: Int = View.GONE){
        mainBinding.progressBar.visibility = view
    }

    // THIS OVERRIDE METHODE USE TO HANDLE BACK PRESS

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (mainViewModel.type != "top"){
            categoryAdapter.selectedPosition = RecyclerView.NO_POSITION
            categoryAdapter.notifyDataSetChanged()
            mainBinding.txtTool.visibility = View.GONE
            mainViewModel.type = "top"
            mainViewModel.toolVisibility = false
            getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
        }else{
            super.onBackPressed()
        }
    }

    // THIS OVERRIDE METHODE USE TO SEARCH CATEGORY BY NEWS
    override fun selectCategoy(text:String,position: Int) {
       mainViewModel._selectedPosition = position
       if(mainViewModel.cats != text){
           mainViewModel.cats = text
           mainViewModel.type = "category"
           getNews(mainViewModel.type,mainViewModel.cats,mainViewModel.searchText)
       }
        if(mainBinding.filterLayout.visibility != View.VISIBLE){
            mainBinding.txtTool.visibility = View.VISIBLE
            mainViewModel.toolVisibility = true
        }else mainViewModel.toolVisibility = false

    }

    // THIS OVERRIDE METHODE USE TO READ MORE ABOUT NEWS IN NEXT ACTIVITY
    override fun readMore(url: String,title:String) {
        val intent = Intent(this,NewsDetail::class.java)
        intent.putExtra("url",url)
        intent.putExtra("title",title)
        startActivity(intent)
    }

    // THIS METHODE USE FOR SETUP DROPDOWN OF FILTER
    private fun dropdown(view: Spinner, list:Array<String>, short:Int){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.adapter = adapter
       view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
              if(short==1){
                  if(list[position]=="recently"){
                      mainViewModel.shortBy  = "publishedAt"
                  }else{
                      mainViewModel.shortBy  = list[position]
                  }

              }else{
                  mainViewModel.shortByCountry  = list[position]
              }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }

    }


}