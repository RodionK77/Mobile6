package com.example.storedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storedata.data.DBHelper;
import com.example.storedata.data.DBManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mySettings";
    public final static String EXTRA_MESSAGE = "com.company.myriads.MESSAGE";
    private DBManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDbManager = new DBManager(this);
    }

    public String[] getText(){
        EditText keyET1 = (EditText) findViewById(R.id.keyOneMessage);
        EditText valueET1 = (EditText) findViewById(R.id.valueOneMessage);
        EditText keyET2 = (EditText) findViewById(R.id.keyTwoMessage);
        EditText valueET2 = (EditText) findViewById(R.id.valueTwoMessage);
        EditText keyET3 = (EditText) findViewById(R.id.keyThreeMessage);
        EditText valueET3 = (EditText) findViewById(R.id.valueThreeMessage);
        String[] str = new String[6];
        str[0] = keyET1.getText().toString();
        str[1] = valueET1.getText().toString();
        str[2] = keyET2.getText().toString();
        str[3] = valueET2.getText().toString();
        str[4] = keyET3.getText().toString();
        str[5] = valueET3.getText().toString();
        return str;
    }

    public void sendMessage(View view){
        String[] et = getText();
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(et[0], et[1]);
        editor.putString(et[2], et[3]);
        editor.putString(et[4], et[5]);
        editor.apply();

        String str = "";
        Map<String, ?> allEntries = mSettings.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            str = str + entry.getKey() + ": " + entry.getValue().toString() + "\n";
        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }


    public static String bytesToHuman (long size)
    {
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

    public String getFreeMemory(File path)
    {
        StatFs stats = new StatFs(path.getAbsolutePath());
        long free = stats.getAvailableBlocksLong() * stats.getBlockSizeLong();
        return bytesToHuman(free);
    }

    public void writeInternal(View view) {
        String[] et = getText();
        String str = "";
        for (int i = 0; i<6;i++){
            str += et[i] + " ";
            if(i%2!=0){
                str+="\n";
            }
        }
        FileOutputStream out;
        try{
            out = openFileOutput("fileS", MODE_PRIVATE);
            out.write(str.getBytes());
            File file = new File(getFilesDir(), "fileS");
            file.delete();
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Осталось свободного пространства: " + getFreeMemory(Environment.getDataDirectory()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteInternal(View view) {
        try {
            File file = new File(getFilesDir(), "fileS");
            file.delete();
            Toast.makeText(this, "Файл fileS удалён", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File getExternalPath() {
        return new File(getExternalFilesDir(null), "document");
    }

    public void writeExternal(View view) {
        String[] et = getText();
        String str = "";
        for (int i = 0; i<6;i++){
            str += et[i] + " ";
            if(i%2!=0){
                str+="\n";
            }
        }
        try(FileOutputStream fos = new FileOutputStream(getExternalPath())) {
            fos.write(str.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void writeBd(View view){
        String[] et = getText();
        myDbManager.openDB();
        for (int i = 0; i<6;i+=2){
            myDbManager.insertDB(et[i], et[i+1]);
        }
        Toast.makeText(this, "Записанные данные:\n" + myDbManager.getDB(), Toast.LENGTH_SHORT).show();
        myDbManager.deleteDB();
    }

    @Override
    protected void onDestroy(){
        myDbManager.closeDB();
        super.onDestroy();
    }

}