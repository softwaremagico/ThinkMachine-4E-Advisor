package com.softwaremagico.tm.advisor.ui.components.spinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.R;

import java.io.Serializable;
import java.util.List;

public class SearchableListDialog<E extends Element> extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";

    private ArrayAdapter<E> listAdapter;

    private ListView listViewItems;

    private SearchableItem<E> searchableItem;

    private OnSearchTextChanged onSearchTextChanged;

    private SearchView searchView;

    private DialogInterface.OnClickListener onClickListener;


    public static SearchableListDialog newInstance(List items) {
        SearchableListDialog multiSelectExpandableFragment = new
                SearchableListDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);

        multiSelectExpandableFragment.setArguments(args);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Getting the layout inflater to inflate the view in an alert dialog.
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            searchableItem = (SearchableItem<E>) savedInstanceState.getSerializable("item");
        }
        // Change End

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        alertDialog.setView(rootView);

        String closeButton = getContext().getString(R.string.close);
        alertDialog.setPositiveButton(closeButton, onClickListener);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    // Crash on orientation change #7
    // Change Start
    // Description: Saving the instance of searchable item instance.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", searchableItem);
        super.onSaveInstanceState(outState);
    }

    public void setCloseButton(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem<E> searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        searchView = rootView.findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


        listViewItems = rootView.findViewById(R.id.listItems);

        //attach the adapter to the list
        listViewItems.setAdapter(listAdapter);

        listViewItems.setTextFilterEnabled(true);

        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
            getDialog().dismiss();
            //Remove filter.
            ((ArrayAdapter<E>) listViewItems.getAdapter()).getFilter().filter(null);
        });
    }

    public void setAdapter(ArrayAdapter<E> adapter) {
        listAdapter = adapter;
    }

    @Override
    public boolean onClose() {
        ((ArrayAdapter<E>) listViewItems.getAdapter()).getFilter().filter(null);
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ArrayAdapter<E>) listViewItems.getAdapter()).getFilter().filter(null);
        dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            ((ArrayAdapter<E>) listViewItems.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter<E>) listViewItems.getAdapter()).getFilter().filter(s);
        }
        if (onSearchTextChanged != null) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }
}
