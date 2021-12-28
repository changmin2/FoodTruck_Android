//package org.techtown.navagation.other;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.techtown.navagation.R;
//
//public class Fragment1 extends Fragment {
//    TextView author;
//    TextView title;
//    TextView content;
//    PersonAdapter adapter;
//    Cursor cursor;
//    Uri uri;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//
//
//        View root =  inflater.inflate(R.layout.fragment1,container,false);
//        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new PersonAdapter();
//        Button button = root.findViewById(R.id.button);
//        author = root.findViewById(R.id.author);
//        title = root.findViewById(R.id.title);
//        content = root.findViewById(R.id.content);
//        String uriString = "content://org.techtown.navagation/person";
//        uri = new Uri.Builder().build().parse(uriString);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                ContentValues values = new ContentValues();
//                values.put("title",title.getText().toString());
//                values.put("author",author.getText().toString());
//                values.put("content",content.getText().toString());
//                uri = getActivity().getContentResolver().insert(uri,values);
//                adapter.addItem(new Person(title.getText().toString(),author.getText().toString(),content.getText().toString()));
//                adapter.notifyDataSetChanged();
//            }
//        });
//        String [] columns = new String[]{"title","author","content"};
//        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
//        System.out.println("결과"+cursor.getCount());
//        while(cursor.moveToNext()){
//            String get_title = cursor.getString(cursor.getColumnIndex(columns[0]));
//            String get_author = cursor.getString(cursor.getColumnIndex(columns[1]));
//            String get_content = cursor.getString(cursor.getColumnIndex(columns[2]));
//            adapter.addItem(new Person(get_title,get_author,get_content));
//            adapter.notifyDataSetChanged();
//        }
//
//        recyclerView.setAdapter(adapter);
//        return root;
//
//    }
//}
