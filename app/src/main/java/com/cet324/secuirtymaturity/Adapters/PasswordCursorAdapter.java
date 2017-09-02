package com.cet324.secuirtymaturity.Adapters;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cet324.secuirtymaturity.Database.DBHelper;
import com.cet324.secuirtymaturity.R;


public class PasswordCursorAdapter extends CursorAdapter{
    //get references to other classes

    public PasswordCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    @Override
    //infiltrate the layout
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_list_layout_password,viewGroup,false );
    }
    @Override
    //get the correct view and apply the data to the text fields
    public void bindView(View view, Context context, Cursor cursor) {
        //get all text fields in the layout
        TextView txtPass = (TextView) view.findViewById(R.id.txtListPass);
        TextView txtCompany = (TextView) view.findViewById(R.id.txtPassCompany);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDateAdded);


        //apply cursor adapter to these text fields & star rank bar
        txtCompany.setText("Password For: "+cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD_FOR)));
        txtPass.setText("Password: "+cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD_NAME)));
        txtDate.setText("Created Date: "+cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD_DATE)));


    }

}
