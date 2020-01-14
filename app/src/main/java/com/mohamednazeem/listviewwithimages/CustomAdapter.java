package com.mohamednazeem.listviewwithimages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<RowItem> rowItems;
    String result;


    public CustomAdapter(Context context, ArrayList<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(position);
    }


    private class ViewHolder{
        ImageView profilePic;
        TextView title;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        LayoutInflater myInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //if (convertView == null){
            convertView = myInflater.inflate(R.layout.activity_list_item, null);
            holder = new ViewHolder();

            holder.profilePic = convertView.findViewById(R.id.profilePic);
            holder.title = convertView.findViewById(R.id.title);

            final RowItem rowPos = rowItems.get(position);

            holder.profilePic.setMaxHeight(150);
            holder.profilePic.setMaxWidth(150);
            holder.profilePic.setImageBitmap(rowPos.getPic());
            holder.title.setText(rowPos.getTitle());
        //}


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result = rowItems.get(position).getUserId();

                Intent intent = new Intent(context, FullScreenImageActivity.class);

                intent.putExtra("Bitmap", result);

                context.startActivity(intent);

            }
        });

        return convertView;
    }
}
