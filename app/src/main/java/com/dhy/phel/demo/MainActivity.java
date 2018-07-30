package com.dhy.phel.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.dhy.phel.PinnedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private ArrayList<Group> groupList;
    private ArrayList<List<People>> childList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PinnedExpandableListView expandableListView = findViewById(R.id.expandableListView);
        initData();

        MyexpandableListAdapter adapter = new MyexpandableListAdapter(this, groupList, childList);
        expandableListView.setAdapter(adapter);

        // 展开所有group
        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
    }

    /***
     * InitData
     */
    void initData() {
        groupList = new ArrayList<>();
        Group group;
        for (int i = 0; i < 3; i++) {
            group = new Group();
            group.setTitle("group-" + i);
            groupList.add(group);
        }

        childList = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<People> childTemp;
            if (i == 0) {
                childTemp = new ArrayList<>();
                for (int j = 0; j < 13; j++) {
                    People people = new People();
                    people.setName("yy-" + j);
                    people.setAge(30);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else if (i == 1) {
                childTemp = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    People people = new People();
                    people.setName("ff-" + j);
                    people.setAge(40);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            } else {
                childTemp = new ArrayList<>();
                for (int j = 0; j < 23; j++) {
                    People people = new People();
                    people.setName("hh-" + j);
                    people.setAge(20);
                    people.setAddress("sh-" + j);

                    childTemp.add(people);
                }
            }
            childList.add(childTemp);
        }

    }


    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v, int groupPosition, final long id) {
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(MainActivity.this, childList.get(groupPosition).get(childPosition).getName(), Toast.LENGTH_LONG).show();
        return false;
    }
}
