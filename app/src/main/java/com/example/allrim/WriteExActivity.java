package com.example.allrim;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class WriteExActivity extends AppCompatActivity {
    private EditText mEditTitle; //타이틀
    private EditText mEditContent; //content
    private Button btn_insert; //등록 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //insert
        mEditTitle = findViewById(R.id.editTitle);
        mEditContent = findViewById(R.id.editContent);

        btn_insert=findViewById(R.id.bt_writing_submit);
        btn_insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //타이틀 (글제목)
                String title = mEditTitle.getText().toString();
                //글 내용
                String contents = mEditContent.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            //게시글 등록 성공시
                            if(success){
                                Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteExActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 Volley를 이용해서 요청
                InsertRequest registerRequest = new InsertRequest(title,contents, responseListener);
                RequestQueue queue = Volley.newRequestQueue( WriteExActivity.this );
                queue.add( registerRequest );
            }
        });
    }
}