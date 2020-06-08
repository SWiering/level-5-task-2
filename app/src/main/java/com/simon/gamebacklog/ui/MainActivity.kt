package com.simon.gamebacklog.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simon.gamebacklog.R
import com.simon.gamebacklog.model.Game

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val ADD_GAME_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private val games = arrayListOf<Game>()
    private val gamesCache = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()
        initListeners()
    }

    private fun initViews(){
        rvGameReleases.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvGameReleases.adapter = gameAdapter

        // The slide remove thing
        createItemTouchHelper().attachToRecyclerView(rvGameReleases)
    }

    private fun initViewModel() {
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.games.observe(this, Observer { games ->
            this@MainActivity.games.clear()
            this@MainActivity.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })
    }

    private fun initListeners(){
        fabAdd.setOnClickListener{ startAddActivity() }
    }

    private fun startAddActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, ADD_GAME_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_GAME_REQUEST_CODE -> {
                    val game = data!!.getParcelableExtra<Game>(EXTRA_GAME)
                    mainActivityViewModel.insertGame(game)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.delete_games -> {
                // also clear the gamecache in case the user already deleted a whole list of games
                gamesCache.clear()
                gamesCache.addAll(games)

                // Clear the list of games after archiving them
                games.clear()

                mainActivityViewModel.deleteAllGames()
                Snackbar.make(rvGameReleases, "Successfully deleted game", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        gamesCache.forEach {
                            mainActivityViewModel.insertGame(it)
                        }
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val game = games[position]

                if(direction == ItemTouchHelper.LEFT) {
                    mainActivityViewModel.deleteGame(game)
                    Snackbar
                        .make(viewHolder.itemView, "Successfully deleted game", Snackbar.LENGTH_LONG)
                        .setAction("UNDO"){
                            mainActivityViewModel.insertGame(game)
                        }
                        .show()
                }

                gameAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }
}
