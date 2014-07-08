package com.moto.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.DataBaseModel;
import com.moto.utils.DateUtils;
import com.rockerhieu.emojicon.EmojiconTextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 14-7-5.
 */
public class User_SystemSetting_Draftbox extends Moto_RootActivity{
    private ListView listView;
    private String subject = "";
    private DataBaseModel dataBaseModel;
    private List<DataBaseModel> list = new ArrayList<DataBaseModel>();
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.user_system_draftbox,R.string.draftbox,barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);
        init();

        getDataBaseData();

        adapter = new MyAdapter(this,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(list.get(i).type == 1)
//                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data",list.get(i));
                    pushToNextActivity(bundle, User_Draftbox_writeLive.class);
//                }
//                else{
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data",list.get(i));
//                    pushToNextActivity(bundle,User_Draftbox_writepost.class);
//                }
            }
        });
    }

    private void init(){

        listView = (ListView)findViewById(R.id.user_system_drafbox_listview);
    }

    private void getDataBaseData()
    {
       list = new Select().from(DataBaseModel.class).execute();
    }


    //	内部类实现BaseAdapter  ，自定义适配器
    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<DataBaseModel> list;


        public MyAdapter(Context context, List<DataBaseModel> list)
        {
            this.context = context;
            this.list = list;
            //			imageLoader = new ImageLoader(context);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;
            // TODO Auto-generated method stub
            //			if(convertView == null)
            //			{
            convertView = LayoutInflater.from(context).inflate(R.layout.user_system_drafbox_kids, null);
            holder = new ViewHolder();
            holder.user_system_draftbox_kid_time = (TextView)convertView.findViewById(R.id.user_system_draftbox_kid_time);
            holder.subject = (EmojiconTextView)convertView.findViewById(R.id.user_system_draftbox_kid_subject);
            holder.subject.setText(list.get(position).message);
            holder.user_system_draftbox_kid_time.setText(DateUtils.timestampToDeatil(list.get(position).time));
            return convertView;
        }


    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        EmojiconTextView subject;
        TextView user_system_draftbox_kid_time;
    }

}
