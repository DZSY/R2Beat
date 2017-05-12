package com.positif.r2beat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final boolean SINGLE = true;
    final boolean GROUP = false;
    String musicName = new String();
    private RadioGroup radioGroup;
    private boolean select = SINGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏状态栏

        Intent intent = getIntent();
        String msg = intent.getStringExtra("Message");
        if (msg != null)
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.singleradioBtn) {
                    select = SINGLE;
                } else {
                    select = GROUP;
                }
            }
        });


        FloatingActionButton fabOption = (FloatingActionButton) findViewById(R.id.fabOption);
        fabOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "长按选择歌曲", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        registerForContextMenu(fabOption);

        FloatingActionButton fabPractice = (FloatingActionButton) findViewById(R.id.fabPractice);
        fabPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SoloGameActivity.class);
                intent.putExtra("MusicName", "practice");
                startActivity(intent);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterViewCompat.AdapterContextMenuInfo info = (AdapterViewCompat.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.yicijiuhao: {
                musicName = "yicijiuhao";
                break;
            }
            case R.id.pingfanzhilu: {
                musicName = "pingfanzhilu";
                break;
            }
            case R.id.haojiubujian: {
                musicName = "haojiubujian";
                break;
            }
        }
        if (select == SINGLE) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SoloGameActivity.class);
            intent.putExtra("MusicName", musicName);
            this.startActivity(intent);
        } else {
            final EditText input = new EditText(this);
            input.setFocusable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("输入一个昵称吧")
                    .setIcon(R.drawable.dialogicon)
                    .setIconAttribute(R.attr.icon)
                    .setView(input)
                    .setPositiveButton("好的",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String inputName = input.getText().toString();
                                    if (inputName.isEmpty()) {
                                        Toast.makeText(MainActivity.this, "昵称不能是空的哦", Toast.LENGTH_SHORT).show();
                                    } else {
                                        final Intent intent = new Intent();
                                        intent.setClass(MainActivity.this, FightGameActivity.class);
                                        intent.putExtra("MusicName", musicName);
                                        intent.putExtra("Alias", input.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            })
                    .show();
        }
        return true;
    }

}
