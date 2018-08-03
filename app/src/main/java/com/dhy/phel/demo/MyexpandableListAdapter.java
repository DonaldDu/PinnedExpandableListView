package com.dhy.phel.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhy.phel.PinnedExpandableListView;

import java.util.List;

public class MyexpandableListAdapter extends BaseExpandableListAdapter implements PinnedExpandableListView.PinnedGroup {
    private LayoutInflater inflater;
    private List<Group> groupList;
    private List<List<People>> childList;
    private Context context;

    MyexpandableListAdapter(Context context, List<Group> groupList, List<List<People>> childList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    // 返回父列表个数
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 返回子列表个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group, parent, false);
        }
        convertView.setTag(groupPosition);
        GroupHolder groupHolder = (GroupHolder) convertView.getTag(R.layout.group);
        if (groupHolder == null) {
            groupHolder = new GroupHolder();
            groupHolder.textView = convertView.findViewById(R.id.group);
            groupHolder.imageView = convertView.findViewById(R.id.image);
            convertView.setTag(R.layout.group, groupHolder);
        }
        groupHolder.textView.setText(((Group) getGroup(groupPosition)).getTitle());
        if (isExpanded)// ture is Expanded or false is not isExpanded
            groupHolder.imageView.setImageResource(R.drawable.expanded);
        else
            groupHolder.imageView.setImageResource(R.drawable.collapse);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child, parent, false);

            childHolder.textName = convertView.findViewById(R.id.name);
            childHolder.textAge = convertView.findViewById(R.id.age);
            childHolder.textAddress = convertView.findViewById(R.id.address);
            childHolder.imageView = convertView.findViewById(R.id.image);
            Button button = convertView.findViewById(R.id.button1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "clicked pos=", Toast.LENGTH_SHORT).show();
                }
            });

            convertView.setTag(R.layout.child, childHolder);
        }
        childHolder = (ChildHolder) convertView.getTag(R.layout.child);

        childHolder.textName.setText(((People) getChild(groupPosition, childPosition)).getName());
        childHolder.textAge.setText(String.valueOf(((People) getChild(groupPosition, childPosition)).getAge()));
        childHolder.textAddress.setText(((People) getChild(groupPosition, childPosition)).getAddress());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView textName;
        TextView textAge;
        TextView textAddress;
        ImageView imageView;
    }
}
