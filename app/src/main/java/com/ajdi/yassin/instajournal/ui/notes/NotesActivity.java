package com.ajdi.yassin.instajournal.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ajdi.yassin.instajournal.R;
import com.ajdi.yassin.instajournal.ui.addedit.AddEditNoteActivity;
import com.ajdi.yassin.instajournal.ui.notedetail.NoteDetailActivity;
import com.ajdi.yassin.instajournal.utils.ActivityUtils;
import com.ajdi.yassin.instajournal.utils.UiUtils;
import com.ajdi.yassin.instajournal.utils.ViewModelFactory;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NotesActivity extends AppCompatActivity implements NotesNavigator, NoteItemNavigator {

    private NotesViewModel mViewModel;

    private BottomAppBar mBar;

    private BottomSheetBehavior<View> bottomDrawerBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        setupViewFragment();

        setupBottomBar();

        setUpBottomDrawer();


        mViewModel = obtainViewModel(this);

        // Subscribe to add new note event
        mViewModel.getNewNoteEvent().observeEvent(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                addNewNote();
            }
        });

        // Subscribe to open note event
        mViewModel.getOpenNoteEvent().observeEvent(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String noteId) {
                if (noteId != null) {
                    openNoteDetails(noteId);
                }
            }
        });
    }

    private void setUpBottomDrawer() {
        View bottomDrawer = findViewById(R.id.bottom_drawer);
        bottomDrawerBehavior = BottomSheetBehavior.from(bottomDrawer);
        bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
        mBar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        //bar.replaceMenu(R.menu.demo_primary);
    }

    private void setupBottomBar() {
        mBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(mBar);
    }

    private void setupViewFragment() {
        NotesFragment mNotesFragment = (NotesFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (mNotesFragment == null) {
            // Create the fragment
            mNotesFragment = NotesFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            ActivityUtils.replaceFragmentInActivity(fragmentManager,
                    mNotesFragment, R.id.fragment_container);
        }
    }

    public static NotesViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(NotesViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        if (menuItem != null) {
            UiUtils.tintMenuIcon(this, menuItem, R.color.md_white_1000);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewModel.handleActivityResult(requestCode, resultCode);

    }

    @Override
    public void addNewNote() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE);
    }

    @Override
    public void openNoteDetails(String noteId) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId);
        startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE);
    }
}
