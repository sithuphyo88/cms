package com.ideapro.cms.view;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.BluePrintEntity;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.BluePrintListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluePrintListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    BluePrintListAdapter adapter;
    ImageButton imgAdd;
    List<BluePrintEntity> list = null;
    DatePickerDialog datePicker;
    private Date selectedDate;
    Menu menu;

    public BluePrintListFragment() {
        this.siteEntity = new SiteEntity();
    }

    public BluePrintListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if(siteEntity == null) {
            this.siteEntity = new SiteEntity();
            this.projectEntity = new ProjectEntity();
        } else {
            this.siteEntity = siteEntity;
            this.projectEntity = projectEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blueprint_list, container, false);
        setHasOptionsMenu(true);
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_manage_blue_print) + " for " + this.siteEntity.name);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        this.selectedDate = new Date(Calendar.getInstance().getTimeInMillis());
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            adapter = new BluePrintListAdapter(this, view.getContext(), getActivity(), list);
            SwipeMenuListView listView = (SwipeMenuListView)view.findViewById(R.id.listView);
            listView.setDivider(null);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                    deleteItem.setWidth(dp2px(50));
                    deleteItem.setTitle("delete");
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            };

            listView.setMenuCreator(creator);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // delete
                            CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View innerView, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new BluePrintPhotoListFragment());
                }
            });

        } catch (Exception e){
            throw new Error(e);
        }
    }

    public void checkBoxClick(View v) {
        CheckBox cb = (CheckBox) v;
        int id = cb.getId();
        list.get(id).isSelected = cb.isChecked();

        if(hasSelectedItem()) {
            getActivity().getMenuInflater().inflate(R.menu.menu_remove, this.menu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.menu_search, this.menu);
        }
    }

    private boolean hasSelectedItem() {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).isSelected) {
                return true;
            }
        }

        return false;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void showInputDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Categorized by Date");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_DATETIME);
        input.setHint("Enter date");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(getString(R.string.label_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(input.getText())) {
                            Toast.makeText(view.getContext(), getString(R.string.message_date_required), Toast.LENGTH_SHORT).show();
                        } else {
                            BluePrintEntity entity = new BluePrintEntity();
                            entity.name = input.getText().toString();
                            list.add(entity);
                            dialog.cancel();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.label_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        // Create the DatePickerDialog instance
        datePicker = null;
        datePicker = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    boolean isFired = false;
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            view.updateDate(year, monthOfYear, dayOfMonth);
                        }

                        if(!isFired) {
                            isFired = true;
                            Calendar selectedCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                            selectedDate.setTime(selectedCalendar.getTimeInMillis());

                            BluePrintEntity entity = new BluePrintEntity();
                            android.text.format.DateFormat sdf = new android.text.format.DateFormat();
                            entity.name = sdf.format("yyyy-MM-dd", selectedDate).toString();
                            list.add(entity);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));

        datePicker.setCancelable(true);
        // ((ViewGroup) datePicker.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }
}
