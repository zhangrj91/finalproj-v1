package com.example.jason.finalproj.Github;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jason.finalproj.R;

import java.util.List;

/**
 * Created by jason on 2018/1/2.
 */

public class Reposadapter extends BaseAdapter {
    private Context context;
    private List<Repos> ListItems;
    private int ID;
    public Reposadapter(Context context,int ID, List<Repos>Listitems){
        this.context = context;
        this.ID = ID;
        this.ListItems = Listitems;
    }

    public List<Repos> getListItems(){
        return ListItems;
    }

    public void setListItems(List<Repos> listItems) {
        ListItems = listItems;
    }

    public int getCount(){
        return ListItems.size();
    }

    public Object getItem(int position){
        if(ListItems==null)
        {
            return null;
        }
        return ListItems.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    class ListItemView{
        private TextView tv_name;
        private TextView tv_description;
        private TextView tv_language;

        public TextView getTv_name(){
            return tv_name;
        }

        public void setTv_name(TextView tv_name) {
            this.tv_name = tv_name;
        }

        public TextView getTv_description() {
            return tv_description;
        }

        public void setTv_description(TextView tv_description) {
            this.tv_description = tv_description;
        }

        public TextView getTv_language() {
            return tv_language;
        }

        public void setTv_language(TextView tv_language) {
            this.tv_language = tv_language;
        }
    }

    public View getView(int position, View view, ViewGroup parent){
        View convertView;
        ListItemView mylistitemview;
        if(view==null){
            mylistitemview = new ListItemView();
            convertView = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
            mylistitemview.setTv_name((TextView)convertView.findViewById(R.id.name));
            mylistitemview.setTv_language((TextView)convertView.findViewById(R.id.language));
            mylistitemview.setTv_description((TextView)convertView.findViewById(R.id.description));
            convertView.setTag(mylistitemview);
        }else {
            convertView = view;
            mylistitemview = (ListItemView) convertView.getTag();
        }
        mylistitemview.getTv_name().setText(ListItems.get(position).getName());
        mylistitemview.getTv_language().setText(ListItems.get(position).getLanguage());
        mylistitemview.getTv_description().setText(ListItems.get(position).getDescription());
        return convertView;
    }
}