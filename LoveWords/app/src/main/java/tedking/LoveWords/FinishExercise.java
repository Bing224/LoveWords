package tedking.LoveWords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishExercise extends AppCompatActivity {
    private Button comfirm;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_exercise);
        comfirm = (Button) findViewById(R.id.comfirm);
        textView = (TextView) findViewById(R.id.textview);
        Intent intent = getIntent();
        String s = intent.getStringExtra("whetherempty");
        if (s.equals("yes")){
            textView.setText("你已完成该词库的学习，请选择新词库");
        }
        else {
            textView.setText("恭喜你完成所有题目");
        }
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinishExercise.this.finish();
            }
        });
    }
}
