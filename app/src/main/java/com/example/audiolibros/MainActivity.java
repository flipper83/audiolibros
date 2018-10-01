package com.example.audiolibros;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.audiolibros.fragments.DetalleFragment;
import com.example.audiolibros.fragments.SelectorFragment;


public class MainActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {
  public static final String SETTINGS_PREFERENCES = "audiolibros.settings";
  public static final String PREF_LAST_BOOK = "settings.lastBook";
  public static final int TAB_ALL = 0;
  public static final int TAB_NEW = 1;
  public static final int TAB_READ = 2;
  private AdaptadorLibrosFiltro bookAdapter;

  private FloatingActionButton fabButton;
  private AppBarLayout appBarLayout;
  private TabLayout tabsView;
  private DrawerLayout navigationDrawer;
  private ActionBarDrawerToggle toggleActionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bookAdapter = ((Aplicacion) getApplicationContext()).getAdaptador();
    initBooksContain();
    initToolBar();
    initFabButton();
    initTabs();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.menu_prefs) {
      PreferenciasActivity.open(this);
      return true;
    } else if (id == R.id.about_menu) {
      showAboutDialog();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_alls) {
      showCategory(Libro.CATEGORY_ALL);
    } else if (id == R.id.nav_epic) {
      showCategory(Libro.CATEGORY_EPIC);
    } else if (id == R.id.nav_XIX) {
      showCategory(Libro.CATEGORY_XIX);
    } else if (id == R.id.nav_thiller) {
      showCategory(Libro.G_THRILLER);
    } else if (id == R.id.nav_preferences) {
      PreferenciasActivity.open(this);
    }
    closeDrawer();
    return true;
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      closeDrawer();
    } else {
      super.onBackPressed();
    }
  }

  public void openLastVisit() {
    int id = getIdFromSharedPreferences();
    if (id >= 0) {
      showDetail(id);
    } else {
      showErrorNoLastVisit();
    }
  }

  public void showDetail(int id) {
    DetalleFragment detalleFragment = (DetalleFragment)
      getSupportFragmentManager().findFragmentById(R.id.detalle_fragment);
    if (detalleFragment != null) {
      detalleFragment.ponInfoLibro(id);
    } else {
      createDetailFragment(id);
      storeIdOnSharePreferences(id);
    }
  }

  private void createDetailFragment(int id) {
    DetalleFragment detailFragment = DetalleFragment.create(id);
    showFragment(detailFragment);
  }


  public void showDrawer(boolean show) {
    if (appBarLayout != null) {
      appBarLayout.setExpanded(show);
    }
    toggleActionBar.setDrawerIndicatorEnabled(show);
    if (show) {
      navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
      tabsView.setVisibility(View.VISIBLE);
    } else {
      navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      tabsView.setVisibility(View.GONE);
    }
  }

  private void showErrorNoLastVisit() {
    Toast.makeText(this, R.string.no_last_visit_message, Toast.LENGTH_LONG).show();
  }

  private void initTabs() {
    tabsView = findViewById(R.id.tabs);
    tabsView.addTab(tabsView.newTab().setText(R.string.tab_title_all));
    tabsView.addTab(tabsView.newTab().setText(R.string.tab_title_new));
    tabsView.addTab(tabsView.newTab().setText(R.string.tab_title_read));
    tabsView.setTabMode(TabLayout.MODE_SCROLLABLE);
    tabsView.addOnTabSelectedListener(tabSelectedListener);
  }

  private void closeDrawer() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
  }

  private void showCategory(String s) {
    bookAdapter.setGenero(s);
    bookAdapter.notifyDataSetChanged();
  }

  private void initFabButton() {
    fabButton = findViewById(R.id.fab);
    fabButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openLastVisit();
      }
    });
  }

  private void initToolBar() {
    appBarLayout = findViewById(R.id.appBarLayout);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    showBackInActionBar();
    initNavigationDrawer(toolbar);
    setNavigationDrawerListener();
  }

  private void showAboutDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.about_dialog_description);
    builder.setPositiveButton(android.R.string.ok, null);
    builder.create().show();
  }

  private void initNavigationDrawer(Toolbar toolbar) {
    navigationDrawer = findViewById(R.id.drawer_layout);
    toggleActionBar = new ActionBarDrawerToggle(this,
      navigationDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    navigationDrawer.addDrawerListener(toggleActionBar);
    toggleActionBar.syncState();
    toggleActionBar.setToolbarNavigationClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  private void setNavigationDrawerListener() {
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  private void showBackInActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void initBooksContain() {
    if (booksContainerExist()) {
      addBooksFragment();
    }
  }

  private void showFragment(Fragment fragment) {
    FragmentTransaction transaccion = getSupportFragmentManager()
      .beginTransaction();
    transaccion.replace(R.id.books_container, fragment);
    transaccion.addToBackStack(null);
    transaccion.commit();
  }

  private void addBooksFragment() {
    SelectorFragment booksFragment = new SelectorFragment();
    getSupportFragmentManager().beginTransaction()
      .add(R.id.books_container, booksFragment).commit();
  }

  private boolean booksContainerExist() {
    return (findViewById(R.id.books_container) != null) &&
      (getSupportFragmentManager().findFragmentById(
        R.id.books_container) == null);
  }

  private void storeIdOnSharePreferences(int id) {
    SharedPreferences pref = getSharedPreferences(
      SETTINGS_PREFERENCES, MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putInt(PREF_LAST_BOOK, id);
    editor.apply();
  }

  private int getIdFromSharedPreferences() {
    SharedPreferences pref = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);
    return pref.getInt(PREF_LAST_BOOK, -1);
  }

  private TabLayout.BaseOnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      switch (tab.getPosition()) {
        case TAB_ALL:
          showAllBooks();
          break;
        case TAB_NEW:
          showNewBooks();
          break;
        case TAB_READ:
          showReadBooks();
          break;
      }
      bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
  };

  private void showReadBooks() {
    bookAdapter.setNovedad(false);
    bookAdapter.setLeido(true);
  }

  private void showNewBooks() {
    bookAdapter.setNovedad(true);
    bookAdapter.setLeido(false);
  }

  private void showAllBooks() {
    bookAdapter.setNovedad(false);
    bookAdapter.setLeido(false);
  }

}
